package com.luisma.pactype.views.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.enemyColorByType
import com.luisma.pactype.views.game.states.GameEnemyState
import kotlinx.collections.immutable.ImmutableList


@Composable
fun EnemiesTarget(
    tileSize: Dp,
    enemies: ImmutableList<GameEnemyState>
) {
    for (idx in enemies.lastIndex downTo 0) {
        val enemy = enemies[idx]
        EnemyTarget(
            color = enemyColorByType(enemyType = enemy.enemyType),
            tileSize = tileSize,
            coordinate = enemy.targetCoordinate
        )
    }
}

@Composable
fun EnemyTarget(
    color: Color,
    tileSize: Dp,
    coordinate: Coordinate,
) {
    Box(
        modifier = Modifier
            .size(tileSize)
            .offset(
                x = tileSize * coordinate.first,
                y = tileSize * coordinate.second
            )
            .clip(CircleShape)
            .background(color = color)
    )
}