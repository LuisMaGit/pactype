package com.luisma.pactype.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.theme.P_FONT_SIZE_24
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp

@Composable
fun DialogTitle(
    modifier: Modifier = Modifier,
    color: Color,
    text: String
) {
    PText(
        modifier = modifier.padding(
            bottom = 30.dp
        ),
        text = text,
        fontSize = P_FONT_SIZE_24.fontSizeNonScaledSp,
        color = color
    )
}