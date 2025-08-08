package com.luisma.pactype.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp

const val PROGRESS_BAR_DEF_W = 220
const val PROGRESS_BAR_DEF_H = 20
const val PROGRESS_BAR_BORDER_R = 4

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    width: Int = PROGRESS_BAR_DEF_W,
    height: Int = PROGRESS_BAR_DEF_H,
    borderR: Int = PROGRESS_BAR_BORDER_R,
    completedColor: Color,
    unCompletedColor: Color,
    textColor: Color,
    progress: Int,
) {

    Box(
        modifier = modifier
            .width(width.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(borderR.dp))
            .background(unCompletedColor),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .width((width.dp * progress) / 100)
                .height(height.dp)
                .background(completedColor),
        )
        PText(
            modifier = Modifier.align(Alignment.Center),
            text = "${progress}%",
            color = textColor,
            fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp
        )
    }

}