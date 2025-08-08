package com.luisma.pactype.views.game.states

import androidx.compose.runtime.Immutable

@Immutable
data class GameStatusState(
    val status: GameStatus,
    val practiceMode: Boolean,
    val lostShakeAnimationFactor: Int,
) {
    companion object {
        fun initial(): GameStatusState {
            return GameStatusState(
                status = GameStatus.Loading,
                practiceMode = false,
                lostShakeAnimationFactor = 0,
            )
        }
    }

    val acceptKeyboardEvents: Boolean
        get() = status == GameStatus.ReadyToPlay || status == GameStatus.Play
}

enum class GameStatus {
    Loading,
    ReadyToPlay,
    Play,
    Pause,
    Win,
    Lost
}