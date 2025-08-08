package com.luisma.pactype.views.game.states

import androidx.compose.runtime.Immutable


@Immutable
data class GameKeyboardState(
    val capital: Boolean,
    val showKeyboard: Boolean
) {
    companion object {
        fun initial(): GameKeyboardState {
            return GameKeyboardState(
                capital = false,
                showKeyboard = false,
            )
        }
    }
}
