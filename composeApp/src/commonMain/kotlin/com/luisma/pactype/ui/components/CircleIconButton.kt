package com.luisma.pactype.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


const val CIRCLE_ICON_BTN_SMALL_SIZE = 37
const val CIRCLE_ICON_BTN_BIG_SIZE = 53
const val CIRCLE_ICON_BTN_SMALL_ICON_SIZE = 16
const val CIRCLE_ICON_BTN_BIG_ICON_SIZE = 24

@Composable
fun CircleIconButton(
    modifier: Modifier = Modifier,
    size: Int = CIRCLE_ICON_BTN_SMALL_SIZE,
    sizeIcon: Int = CIRCLE_ICON_BTN_SMALL_ICON_SIZE,
    color: Color,
    icon: DrawableResource,
    colorIcon: Color,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(size.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(sizeIcon.dp),
            painter = painterResource(icon),
            colorFilter = ColorFilter.tint(color = colorIcon),
            contentDescription = "",
        )
    }
}