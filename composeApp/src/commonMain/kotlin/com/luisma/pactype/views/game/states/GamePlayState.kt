package com.luisma.pactype.views.game.states

import androidx.compose.runtime.Immutable
import com.luisma.pactype.views.game.data.Coordinate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class GamePlayState(
    val player: PlayerState,
    val enemies: ImmutableList<GameEnemyState>,
    val showEnemyTargetCell: Boolean,
    val playWithRobot : Boolean,
) {
    companion object {
        fun initial(): GamePlayState {
            return GamePlayState(
                player = PlayerState.initial(),
                enemies = persistentListOf(),
                showEnemyTargetCell = false,
                playWithRobot = true,
            )
        }
    }
}

@Immutable
data class PlayerState(
    val coordinates: Coordinate,
    val prevCoordinate: Coordinate
) {
    companion object {
        fun initial(): PlayerState {
            return PlayerState(
                coordinates = Coordinate(0, 0),
                prevCoordinate = Coordinate(0,0)
            )
        }
    }
}
