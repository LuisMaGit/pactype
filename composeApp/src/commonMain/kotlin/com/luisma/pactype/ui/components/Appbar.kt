package com.luisma.pactype.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val APPBAR_H = 40.dp
const val APPBAR_BTN_SIZE = 20

@Composable
fun Appbar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.height(APPBAR_H),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}