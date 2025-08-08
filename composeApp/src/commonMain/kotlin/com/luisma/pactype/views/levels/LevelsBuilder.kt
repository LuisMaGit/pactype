package com.luisma.pactype.views.levels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luisma.pactype.ui.theme.DefaultColors
import com.luisma.pactype.ui.theme.levelThemeConverter
import com.luisma.pactype.views.levels.states.LevelsStatus
import com.luisma.pactype.views.settings_dialog.SettingsDialog
import com.luisma.pactype.views.settings_dialog.SettingsDialogViewModel
import com.luisma.pactype.views.tutorial_dialog.TutorialDialog

@Composable
fun LevelsBuilder(modifier: Modifier = Modifier) {
    val levelsViewModel = viewModel<LevelsViewModel>(factory = LevelsViewModel.Factory)

    val statusState by levelsViewModel.statusState.collectAsState()
    val levelsState by levelsViewModel.levelsState.collectAsState()
    val logoState by levelsViewModel.logoAnimationState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        if (statusState.levelInit) {
            return@LaunchedEffect
        }
        levelsViewModel.sendEvent(LevelsEvents.OnCreate)
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            if (statusState.levelInit) {
                levelsViewModel.sendEvent(LevelsEvents.OnDispose)
            }
        }
    }

    val success = statusState.status == LevelsStatus.Success

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DefaultColors.background),
        contentAlignment = Alignment.TopCenter
    ) {
        if (success) {
            val level = levelsState.levels[levelsState.currentOnScreen - 1]
            val levelTheme = levelThemeConverter(level.levelTheme)
            Levels(
                levelsState = levelsState,
                logoState = logoState,
                statusState = statusState,
                sendEvent = { event -> levelsViewModel.sendEvent(event) }
            )
            if (statusState.showSettingsDialog) {
                val settingsDialogViewModel = viewModel<SettingsDialogViewModel>(
                    factory = SettingsDialogViewModel.Factory
                )
                val settingsDialogState by settingsDialogViewModel.state.collectAsState()
                SettingsDialog(
                    modifier = Modifier.align(Alignment.Center),
                    levelTheme = levelTheme,
                    sendEvent = { event -> settingsDialogViewModel.sendEvent(event) },
                    state = settingsDialogState,
                    onCloseDialog = { levelsViewModel.sendEvent(LevelsEvents.ToggleSettingsDialog) },
                    onOpenTutorial = { levelsViewModel.sendEvent(LevelsEvents.ToggleTutorialDialog) }
                )
            }
            if (statusState.showTutorialDialog) {
                TutorialDialog(
                    modifier = Modifier.align(Alignment.Center),
                    levelTheme = levelTheme,
                    onCloseDialog = { levelsViewModel.sendEvent(LevelsEvents.ToggleTutorialDialog) }
                )
            }
        }
    }


}