package com.luisma.pactype.views.game.states

import androidx.compose.runtime.Immutable
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.data.EnemyMode
import com.luisma.pactype.views.game.data.EnemyType


@Immutable
data class GameEnemyState(
    val enemyType: EnemyType,
    val mode: EnemyMode,
    val coordinates: Coordinate,
    val prevCoordinate: Coordinate,
    val targetCoordinate: Coordinate,
    val startTrigger: Boolean,
    val moveDelayMS : Int,
    val startMoveDelaySec : Int,
) {
    val startNextEnemyTrigger: Boolean
        get() = mode == EnemyMode.Chasing

    val startMovingTrigger: Boolean
        get() = mode == EnemyMode.WaitingInHome && !startTrigger
}

