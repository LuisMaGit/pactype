package com.luisma.pactype.views.game.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luisma.pactype.views.game.data.X_TILE_COUNT
import com.luisma.pactype.views.game.data.Y_TILE_COUNT


@Composable
fun Grid(
    tileSize: Dp,
) {
    for (y in 0..<Y_TILE_COUNT) {
        for (x in 0..<X_TILE_COUNT) {
            Box(
                modifier = Modifier
                    .size(tileSize)
                    .offset(x = tileSize * x, y = tileSize * y)
                    .border(width = 0.5.dp, color = Color.Green),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$x;$y", fontSize = 6.sp, color = Color.Red)
            }
        }
    }
}