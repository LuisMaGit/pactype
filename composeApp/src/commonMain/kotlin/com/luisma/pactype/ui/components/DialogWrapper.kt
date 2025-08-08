package com.luisma.pactype.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DialogWrapper(
    modifier: Modifier = Modifier,
    color: Color,
    borderColor: Color,
    titleColor: Color,
    title: String,
    headerButtonIconColor: Color? = null,
    onClose: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .size(250.dp)
            .border(width = 2.dp, shape = shape, color = borderColor)
            .background(color),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogHeader(
                buttonIconColor = headerButtonIconColor,
                onClose = onClose
            )
            DialogTitle(
                text = title,
                color = titleColor
            )
            content()
        }
    }
}