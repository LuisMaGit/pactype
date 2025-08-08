package com.luisma.pactype.views.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luisma.pactype.ui.theme.levelThemeConverter
import com.luisma.pactype.views.game.states.GameStatus
import com.luisma.pactype.views.game_dialog.GameDialog
import com.luisma.pactype.views.game_dialog.GameDialogType

@Composable
fun GameBuilder(levelId: Int, practiceMode: Boolean) {
    val gameViewModel = viewModel<GameViewModel>(factory = GameViewModel.Factory)
    val mapState by gameViewModel.mapState.collectAsState()
    val playState by gameViewModel.playState.collectAsState()
    val statusState by gameViewModel.statusState.collectAsState()
    val timeState by gameViewModel.timeState.collectAsState()
    val progressState by gameViewModel.progressState.collectAsState()
    val keyboardState by gameViewModel.keyboardState.collectAsState()
    val themeState by gameViewModel.themeState.collectAsState()

    val levelTheme = levelThemeConverter(themeState)

    LaunchedEffect(key1 = levelId) {
        gameViewModel.sendEvent(GameEvents.OnCreate(levelId = levelId, practiceMode = practiceMode))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = levelTheme.background),
    ) {
        if (statusState.status != GameStatus.Loading) {
            Game(
                mapState = mapState,
                playState = playState,
                statusState = statusState,
                timeState = timeState,
                progressState = progressState,
                keyboardState = keyboardState,
                levelTheme = levelTheme,
                sendEvent = { event -> gameViewModel.sendEvent(event) },
            )
            if (statusState.status == GameStatus.Win ||
                statusState.status == GameStatus.Pause
            ) {
                GameDialog(
                    modifier = Modifier.align(Alignment.Center),
                    timeCS = timeState.playingTimeCS,
                    progress = progressState.progress,
                    levelName = mapState.levelName,
                    type = if (statusState.status == GameStatus.Win) {
                        GameDialogType.Win
                    } else {
                        GameDialogType.Pause
                    },
                    levelTheme = levelTheme,
                    practiceMode = statusState.practiceMode,
                    showNewBest = timeState.onWinBestTimeCS != 0,
                    goToMenu = {
                        gameViewModel.sendEvent(GameEvents.GoBack)
                    },
                    onResume = {
                        gameViewModel.sendEvent(GameEvents.OnPause(setPause = false))
                    },
                    onReset = {
                        gameViewModel.sendEvent(GameEvents.OnReset)
                    }
                )
            }
        }
    }

}