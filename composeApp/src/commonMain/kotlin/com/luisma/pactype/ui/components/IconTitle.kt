package com.luisma.pactype.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconTitle(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    colorIcon: Color,
    colorText: Color,
    text: String,
    iconSize: Dp = 24.dp,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(iconSize),
            painter = painterResource(icon),
            colorFilter = ColorFilter.tint(color = colorIcon),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.padding(end = 8.dp))
        PText(
            text = text,
            color = colorText,
            fontSize = P_FONT_SIZE_12.sp
        )
    }
}