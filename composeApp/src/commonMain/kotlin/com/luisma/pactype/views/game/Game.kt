package com.luisma.pactype.views.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luisma.pactype.ui.components.APPBAR_H
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.components.Timer
import com.luisma.pactype.ui.theme.LevelTheme
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import com.luisma.pactype.ui.utils.ScreenSizeUtil
import com.luisma.pactype.views.game.components.GameAppbar
import com.luisma.pactype.views.game.components.Enemies
import com.luisma.pactype.views.game.components.EnemiesTarget
import com.luisma.pactype.views.game.components.Grid
import com.luisma.pactype.views.game.components.KEYBOARD_MARGIN_BOTTOM
import com.luisma.pactype.views.game.components.KEYBOARD_V_PADDING
import com.luisma.pactype.views.game.components.Keyboard
import com.luisma.pactype.views.game.components.MapImage
import com.luisma.pactype.views.game.components.MapLetters
import com.luisma.pactype.views.game.components.PROGRESS_BAR_H
import com.luisma.pactype.views.game.components.Player
import com.luisma.pactype.views.game.components.PressAnyKey
import com.luisma.pactype.views.game.components.Progress
import com.luisma.pactype.ui.utils.CommonMeasurements
import com.luisma.pactype.views.game.states.GameStatus
import com.luisma.pactype.views.game.states.GameKeyboardState
import com.luisma.pactype.views.game.states.GameMapState
import com.luisma.pactype.views.game.states.GamePlayState
import com.luisma.pactype.views.game.states.GameProgressState
import com.luisma.pactype.views.game.states.GameStatusState
import com.luisma.pactype.views.game.states.GameTimeState
import org.jetbrains.compose.resources.stringResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.game_keyboard_dont_fit

@Composable
fun Game(
    mapState: GameMapState,
    playState: GamePlayState,
    statusState: GameStatusState,
    timeState: GameTimeState,
    progressState: GameProgressState,
    keyboardState: GameKeyboardState,
    levelTheme: LevelThemeCompose,
    sendEvent: (event: GameEvents) -> Unit
) {

    ScreenSizeUtil { breakPoint, screenWidthDp, screenHeightDp ->

        val measurements = GameUIMeasurements.fromScreenSize(breakPoint)
        val tileSize = measurements.commonMeasurements.mapTileSize.dp
        val gameW = measurements.commonMeasurements.gameW.dp
        val gameH = measurements.commonMeasurements.gameH.dp
        val gameStatusIndicatorsH = 50

        var keyboardH = if (keyboardState.showKeyboard) {
            (KEYBOARD_V_PADDING * 3) + (measurements.keyHeight.dp * 4) + KEYBOARD_MARGIN_BOTTOM
        } else {
            0.dp
        }
        val fullGameH = gameH + gameStatusIndicatorsH.dp + APPBAR_H
        val showKeyboard = keyboardState.showKeyboard && screenHeightDp >= fullGameH + keyboardH
        keyboardH = if (showKeyboard) keyboardH else 0.dp
        val paddingH = (screenWidthDp - gameW) / 2
        val paddingV = (screenHeightDp - keyboardH - fullGameH) / 2
        val paddingVTrue = if (paddingV < 0.dp) 0.dp else paddingV

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GameAppbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        horizontal = paddingH,
                    ),
                levelTheme = levelTheme,
                showPause = statusState.status == GameStatus.Play,
                onPause = {
                    sendEvent(GameEvents.OnPause(setPause = true))
                },
                onBack = {
                    sendEvent(GameEvents.GoBack)
                },
                showBack = statusState.status == GameStatus.ReadyToPlay,
            )
            Game(
                modifier = Modifier
                    .padding(
                        horizontal = paddingH,
                        vertical = paddingVTrue,
                    )
                    .padding(top = APPBAR_H),
                mapState = mapState,
                playState = playState,
                statusState = statusState,
                timeState = timeState,
                progressState = progressState,
                gameH = gameH,
                gameW = gameW,
                tileSize = tileSize,
                gameStatusIndicatorsH = gameStatusIndicatorsH,
                measurements = measurements,
                levelTheme = levelTheme,
            )
            if (showKeyboard) {
                Keyboard(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(bottom = KEYBOARD_MARGIN_BOTTOM),
                    keyW = measurements.keyWidth,
                    keyH = measurements.keyHeight,
                    capital = keyboardState.capital,
                    colorKey = levelTheme.key,
                    colorLetter = levelTheme.textOn,
                    fontSize = measurements.keyFont,
                    onTap = { key -> sendEvent(GameEvents.KeyboardTap(key)) },
                )
            }
            if (keyboardState.showKeyboard && !showKeyboard) {
                PText(
                    modifier = Modifier.align(alignment = Alignment.BottomCenter),
                    text = stringResource(Res.string.game_keyboard_dont_fit),
                    fontSize = P_FONT_SIZE_12.sp,
                    color = levelTheme.textOff
                )
            }
        }
    }
}

@Composable
private fun Game(
    modifier: Modifier = Modifier,
    mapState: GameMapState,
    playState: GamePlayState,
    statusState: GameStatusState,
    timeState: GameTimeState,
    progressState: GameProgressState,
    gameH: Dp,
    gameW: Dp,
    tileSize: Dp,
    gameStatusIndicatorsH: Int,
    measurements: GameUIMeasurements,
    levelTheme: LevelThemeCompose,
) {
    Box(
        modifier = modifier
    ) {
        MapImage(
            modifier = Modifier
                .size(
                    width = gameW,
                    height = gameH
                ),
            color = levelTheme.textOff,
            mapType = mapState.mapType,
            statusState = statusState,
        )
        MapLetters(
            map = mapState.map,
            tileSize = tileSize,
            fontSize = measurements.fontSize,
            colorOn = levelTheme.textOn,
            colorOff = levelTheme.textOff,
        )
        if (mapState.showDebugGrid) {
            Grid(
                tileSize = tileSize
            )
        }
        Player(
            tileSize = tileSize,
            coordinate = playState.player.coordinates,
            prevCoordinate = playState.player.prevCoordinate,
            color = levelTheme.main,
            animatePlayer = statusState.status == GameStatus.Play
        )
        Enemies(
            tileSize = tileSize,
            enemies = playState.enemies,
            enemyEyeSize = measurements.enemyEyeSize.dp,
            animateEnemies = statusState.status == GameStatus.Play
        )
        if (playState.showEnemyTargetCell) {
            EnemiesTarget(
                tileSize = tileSize,
                enemies = playState.enemies
            )
        }
        if (statusState.status == GameStatus.ReadyToPlay) {
            PressAnyKey(
                modifier = Modifier
                    .padding(top = gameH)
                    .height(gameStatusIndicatorsH.dp),
                colorPressAny = levelTheme.textOn,
                colorNewBest = levelTheme.main,
                progress = progressState.onLostBestProgress
            )
        }
        if (statusState.status != GameStatus.ReadyToPlay) {
            Box(
                modifier = Modifier
                    .padding(top = gameH)
                    .fillMaxWidth()
                    .height(gameStatusIndicatorsH.dp)
            ) {
                Progress(
                    modifier = Modifier.padding(top = 10.dp),
                    progress = progressState.progress,
                    color = levelTheme.main,
                    gameW = gameW
                )
                Timer(
                    modifier = Modifier
                        .padding(top = PROGRESS_BAR_H.dp + 10.dp)
                        .align(Alignment.Center),
                    timeCS = timeState.playingTimeCS,
                    color = levelTheme.main,
                    unitColor = levelTheme.textOn
                )
            }
        }
    }
}