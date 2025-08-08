package com.luisma.pactype.views.game.states

import androidx.compose.runtime.Immutable

@Immutable
data class GameProgressState(
    val charCounter: Int,
    val progress: Int,
    val onLostBestProgress: Int,
) {
    companion object {
        fun initial(): GameProgressState {
            return GameProgressState(
                charCounter = 0,
                progress = 0,
                onLostBestProgress = 0,
            )
        }
    }
}