package com.luisma.pactype.views.settings_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luisma.pactype.services.AppVersionService
import com.luisma.pactype.services.appVersionService
import com.luisma.pactype.views.levels.service.LevelsService
import com.luisma.pactype.views.levels.service.levelsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsDialogViewModel(
    private val levelsService: LevelsService,
    private val appVersionService: AppVersionService,
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            addInitializer(SettingsDialogViewModel::class) {
                SettingsDialogViewModel(
                    levelsService = levelsService(),
                    appVersionService = appVersionService()
                )
            }
        }
    }

    private val _state = MutableStateFlow(SettingsDialogState.initial())
    val state = _state.asStateFlow()

    fun sendEvent(event: SettingsDialogsEvents) {
        when (event) {
            SettingsDialogsEvents.OnCreate -> onCreate()
            SettingsDialogsEvents.OnDispose -> onDispose()
            is SettingsDialogsEvents.VirtualKeyboard -> virtualKeyboard(on = event.on)
        }
    }

    private fun onCreate() {
        if (_state.value.dialogOpened) {
            return
        }
        _state.update {
            it.copy(dialogOpened = true)
        }
        viewModelScope.launch {
            val showKeyboard = levelsService.getShowKeyboard()
            _state.update {
                it.copy(
                    keyboardOn = showKeyboard,
                    version = appVersionService.version()
                )
            }
        }
    }

    private fun onDispose() {
        _state.value = SettingsDialogState.initial()
    }

    private fun virtualKeyboard(on: Boolean) {
        if (on == _state.value.keyboardOn) {
            return
        }
        _state.update {
            it.copy(keyboardOn = on)
        }
        viewModelScope.launch {
            levelsService.setShowKeyboard(on)
        }
    }


}
