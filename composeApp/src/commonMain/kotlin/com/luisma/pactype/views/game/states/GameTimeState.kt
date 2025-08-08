package com.luisma.pactype.views.game.states

import androidx.compose.runtime.Immutable

@Immutable
data class GameTimeState(
    val playingTimeCS: Int,
    val onWinBestTimeCS: Int
) {
    companion object {
        fun initial(): GameTimeState {
            return GameTimeState(
                playingTimeCS = 0,
                onWinBestTimeCS = 0
            )
        }
    }
}