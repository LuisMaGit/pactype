package com.luisma.pactype

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.luisma.pactype.services.Routes
import com.luisma.pactype.services.routerService
import com.luisma.pactype.views.game.services.gameKeyboardChannelService
import com.luisma.pactype.views.levels.service.levelsKeyboardChannelService
import kotlinx.coroutines.launch
import java.awt.Dimension

fun main() = application {

    val gameKeyboardService = gameKeyboardChannelService()
    val gameKeyboardCoroutineScope = rememberCoroutineScope()
    val levelsKeyboardService = levelsKeyboardChannelService()
    val levelsKeyboardCoroutineScope = rememberCoroutineScope()
    val routerService = routerService()

    fun onGameKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyDown) {

            val char = gameKeyboardService.validateChar(
                char = event.key.nativeKeyCode.toChar(),
                isShiftPressed = event.isShiftPressed,
            )

            if (char != null) {
                gameKeyboardCoroutineScope.launch {
                    gameKeyboardService.emitKey(char)
                }
            }
            return true
        }
        return false
    }

    fun onLevelKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyDown) {
            val charIdx = levelsKeyboardService.validateChar(
                char = event.key.nativeKeyCode.toChar()
            )

            if (charIdx == -1) {
                return false
            }

            levelsKeyboardCoroutineScope.launch {
                levelsKeyboardService.sendChar(charIdx)
            }
            return true
        }
        return false
    }

    fun onKeyEvent(event: KeyEvent): Boolean {
        return when (routerService.currentRoute) {
            is Routes.Game -> onGameKeyEvent(event)
            is Routes.Levels -> onLevelKeyEvent(event)
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Pactype",
        state = WindowState(
            width = 750.dp,
            height = 800.dp
        ),
        onKeyEvent = { event -> onKeyEvent(event) }
    ) {
        window.minimumSize = Dimension(360, 500)
        App()
    }
}