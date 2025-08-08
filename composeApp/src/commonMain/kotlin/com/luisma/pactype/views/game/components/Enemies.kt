package com.luisma.pactype.views.game.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.luisma.pactype.ui.theme.DefaultColors
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.enemyColorByType
import com.luisma.pactype.views.game.states.GameEnemyState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun Enemies(
    tileSize: Dp,
    enemyEyeSize: Dp,
    enemies: ImmutableList<GameEnemyState>,
    animateEnemies: Boolean
) {
    for (idx in enemies.lastIndex downTo 0) {
        val enemy = enemies[idx]
        Enemy(
            color = enemyColorByType(enemyType = enemy.enemyType),
            tileSize = tileSize,
            coordinate = enemy.coordinates,
            previousCoordinate = enemy.prevCoordinate,
            moveDelay = enemy.moveDelayMS,
            animate = animateEnemies,
            enemyEyeSize = enemyEyeSize,
        )
    }
}

@Composable
fun Enemy(
    color: Color,
    tileSize: Dp,
    enemyEyeSize: Dp,
    coordinate: Coordinate,
    previousCoordinate: Coordinate,
    moveDelay: Int,
    animate: Boolean
) {
    val offsetX by animateDpAsState(
        targetValue = if (coordinate.first != previousCoordinate.first) {
            tileSize * coordinate.first
        } else {
            tileSize * previousCoordinate.first
        },
        animationSpec = tween(durationMillis = moveDelay, easing = LinearEasing)
    )

    val offsetY by animateDpAsState(
        targetValue = if (coordinate.second != previousCoordinate.second) {
            tileSize * coordinate.second
        } else {
            tileSize * previousCoordinate.second
        },
        animationSpec = tween(durationMillis = moveDelay, easing = LinearEasing)
    )

    Box(
        modifier = Modifier
            .size(tileSize)
            .offset(
                x = if (animate) offsetX else tileSize * coordinate.first,
                y = if (animate) offsetY else tileSize * coordinate.second,
            )
            .background(color = color)
    ) {
        // left eye
        EnemyEye(
            enemyEyeSize = enemyEyeSize,
            x = enemyEyeSize,
            y = enemyEyeSize
        )
        // right eye
        EnemyEye(
            enemyEyeSize = enemyEyeSize,
            x = tileSize - (enemyEyeSize * 2),
            y = enemyEyeSize
        )
        // mouth
        Box(
            modifier = Modifier
                .width(tileSize - (enemyEyeSize * 2))
                .height(enemyEyeSize)
                .offset(x = enemyEyeSize, y = enemyEyeSize * 3)
                .background(color = DefaultColors.enemyFeatures)
        )
    }
}


@Composable
fun EnemyEye(enemyEyeSize: Dp, x: Dp, y: Dp) {
    Box(
        modifier = Modifier
            .size(enemyEyeSize)
            .offset(
                x = x,
                y = y,
            )
            .background(color = DefaultColors.enemyFeatures)
    )
}