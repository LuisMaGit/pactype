package com.luisma.pactype.views.game.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


const val PROGRESS_BAR_H = 4

@Composable
fun Progress(
    modifier: Modifier = Modifier,
    gameW: Dp,
    progress: Int,
    color: Color
) {

    val progressDp by animateDpAsState(
        targetValue = gameW * progress / 100,
        animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
    )

    Box(
        modifier = modifier
            .width(progressDp)
            .height(PROGRESS_BAR_H.dp)
            .background(color)
    )
}