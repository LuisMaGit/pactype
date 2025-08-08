package com.luisma.pactype.views.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luisma.pactype.services.RouterService
import com.luisma.pactype.services.Routes
import com.luisma.pactype.services.routerService
import com.luisma.pactype.views.levels.data.LevelsNavigationKeys
import com.luisma.pactype.views.levels.service.LevelsKeyboardChannelService
import com.luisma.pactype.views.levels.service.LevelsService
import com.luisma.pactype.views.levels.service.levelsKeyboardChannelService
import com.luisma.pactype.views.levels.service.levelsService
import com.luisma.pactype.views.levels.states.LevelsLogoState
import com.luisma.pactype.views.levels.states.LevelsState
import com.luisma.pactype.views.levels.states.LevelsStatus
import com.luisma.pactype.views.levels.states.LevelsStatusState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LevelsViewModel(
    private val levelsService: LevelsService,
    private var routerService: RouterService,
    private var levelsKeyboardService: LevelsKeyboardChannelService
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            addInitializer(LevelsViewModel::class) {
                LevelsViewModel(
                    levelsService = levelsService(),
                    routerService = routerService(),
                    levelsKeyboardService = levelsKeyboardChannelService()
                )
            }
        }
    }

    private var onGoToPageCallback: (suspend (page: Int) -> Unit)? = null

    // states
    private val _statusState = MutableStateFlow(LevelsStatusState.initial())
    val statusState = _statusState.asStateFlow()

    private val _levelsState = MutableStateFlow(LevelsState.initial())
    val levelsState = _levelsState.asStateFlow()

    private val _logoAnimationState = MutableStateFlow(LevelsLogoState.initial())
    val logoAnimationState = _logoAnimationState.asStateFlow()

    fun sendEvent(event: LevelsEvents) {
        if (_statusState.value.dialogOpen && !event.isDialogEvent()) {
            return
        }

        when (event) {
            LevelsEvents.OnCreate -> onCreate()
            LevelsEvents.ToggleSettingsDialog -> toggleSettingsDialog()
            LevelsEvents.ToggleTutorialDialog -> toggleTutorialDialog()
            LevelsEvents.OnDispose -> onDispose()
            is LevelsEvents.SetLevelOnScreen -> setLevelOnScreen(id = event.id)
            is LevelsEvents.OnCreateLevel -> onCreateLevel(goToPageCallback = event.goToPageCallback)
            is LevelsEvents.GoToPage -> goToPage(event.page)
            is LevelsEvents.GoToLastPlayed -> goToLastPlayed()
            is LevelsEvents.GoToGame -> goToGame(
                levelId = event.levelId,
                practiceMode = event.practiceMode
            )
        }
    }

    private fun animateLogo() {
        viewModelScope.launch {
            val chars = _logoAnimationState.value.chars
            val letterDelay = _logoAnimationState.value.charToggleDelayMS.toLong()
            val charsCopy = chars.map { it }.toMutableList()
            var caretOffset = 0
            for (idx in 0 until chars.size) {
                charsCopy[idx] = charsCopy[idx].copy(
                    done = true
                )
                caretOffset += _logoAnimationState.value.charWidth + if (idx == chars.size - 1) 2 else 0
                _logoAnimationState.update {
                    it.copy(
                        chars = charsCopy.toImmutableList(),
                        caretOffset = caretOffset,
                        caretOffsetPrev = _logoAnimationState.value.caretOffset,
                    )
                }
                delay(timeMillis = letterDelay)
                if (idx == chars.size - 1) {
                    _logoAnimationState.update {
                        it.copy(
                            animationDone = true
                        )
                    }
                }
            }
        }
    }

    private suspend fun listenKeyboard() {
        levelsKeyboardService.keyChannelFlow.collect { key ->
            when (key) {
                LevelsNavigationKeys.PREVIOUS -> {
                    if (_levelsState.value.currentOnScreen == 1) {
                        return@collect
                    }
                    goToPage(page = _levelsState.value.currentOnScreen - 2)
                }

                LevelsNavigationKeys.NEXT -> {
                    if (_levelsState.value.currentOnScreen == _levelsState.value.total) {
                        return@collect
                    }
                    goToPage(page = _levelsState.value.currentOnScreen)
                }

                LevelsNavigationKeys.NORMAL_MODE -> {
                    goToGame(levelId = _levelsState.value.currentOnScreen, practiceMode = false)
                }

                LevelsNavigationKeys.PRACTICE_MODE -> {
                    goToGame(levelId = _levelsState.value.currentOnScreen, practiceMode = true)
                }

                LevelsNavigationKeys.SETTINGS -> {
                    _statusState.update {
                        it.copy(
                            showSettingsDialog = !_statusState.value.showSettingsDialog
                        )
                    }
                }

                LevelsNavigationKeys.GO_CURRENTLY -> {
                    if (_levelsState.value.lastPlayed != _levelsState.value.currentOnScreen) {
                        goToLastPlayed()
                    }
                }
            }
        }
    }

    // events
    private fun onCreate() {
        viewModelScope.launch {
            async { _levelsState.value = levelsService.getAllLevels() }.await()
            _statusState.update {
                it.copy(
                    status = LevelsStatus.Success
                )
            }
            animateLogo()
            listenKeyboard()
        }
    }

    private fun onCreateLevel(goToPageCallback: suspend (page: Int) -> Unit) {
        if (_statusState.value.levelInit) {
            return
        }

        onGoToPageCallback = goToPageCallback
        _statusState.update {
            it.copy(levelInit = true)
        }
        if (_levelsState.value.openedAppForFirstTime) {
            _statusState.update {
                it.copy(
                    showTutorialDialog = true
                )
            }
        }
    }

    private fun onDispose() {
        _logoAnimationState.value = LevelsLogoState.initial()
        _levelsState.value = LevelsState.initial()
        _statusState.value = LevelsStatusState.initial()
        onGoToPageCallback = null
    }

    private fun toggleSettingsDialog() {
        _statusState.update {
            it.copy(showSettingsDialog = !_statusState.value.showSettingsDialog)
        }
    }

    private fun toggleTutorialDialog() {
        if (_statusState.value.showSettingsDialog) {
            _statusState.update {
                it.copy(showSettingsDialog = false)
            }
        }
        _statusState.update {
            it.copy(showTutorialDialog = !_statusState.value.showTutorialDialog)
        }
    }

    private fun setLevelOnScreen(id: Int) {
        if (id == _levelsState.value.currentOnScreen) {
            return
        }

        _levelsState.update {
            it.copy(
                currentOnScreen = id,
            )
        }
        _statusState.update {
            it.copy(
                showLastPlayedBanner = id != _levelsState.value.lastPlayed
            )
        }
    }

    private fun goToPage(page: Int) {
        viewModelScope.launch {
            if (onGoToPageCallback != null) {
                onGoToPageCallback!!(page)
            }
        }
    }

    private fun goToLastPlayed() {
        viewModelScope.launch {
            if (onGoToPageCallback != null) {
                onGoToPageCallback!!(_levelsState.value.lastPlayed - 1)
            }
        }
    }

    private fun goToGame(levelId: Int, practiceMode: Boolean) {
        viewModelScope.launch {
            routerService.goTo(
                Routes.Game(levelId = levelId, practiceMode = practiceMode)
            )
        }
    }

}