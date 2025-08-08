package com.luisma.pactype.views.game.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.luisma.pactype.views.game.data.Coordinate

@Composable
fun Player(
    modifier: Modifier = Modifier,
    tileSize: Dp,
    coordinate: Coordinate,
    prevCoordinate: Coordinate,
    color: Color,
    animatePlayer: Boolean,
) {

    val animationDuration = 100
    val animationEasing = LinearEasing

    val offsetX by animateDpAsState(
        targetValue = if (coordinate.first != prevCoordinate.first) {
            tileSize * coordinate.first
        } else {
            tileSize * prevCoordinate.first
        },
        animationSpec = tween(durationMillis = animationDuration, easing = animationEasing)
    )

    val offsetY by animateDpAsState(
        targetValue = if (coordinate.second != prevCoordinate.second) {
            tileSize * coordinate.second
        } else {
            tileSize * prevCoordinate.second
        },
        animationSpec = tween(durationMillis = animationDuration, easing = animationEasing)
    )

    Box(
        modifier = modifier
            .size(tileSize)
            .offset(
                x = if (animatePlayer) offsetX else tileSize * coordinate.first,
                y = if (animatePlayer) offsetY else tileSize * coordinate.second,
            )
            .background(color = color)
    )
}