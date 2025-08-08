package com.luisma.pactype.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun containerSize() : IntSize {
    return LocalWindowInfo.current.containerSize
}

@Composable
actual fun getScreenWidth(): Dp {
    //TODO: fix scale factor 2
    return (containerSize().width.toDouble() / 2).dp
}

@Composable
actual fun getScreenHeight(): Dp {
    //TODO: fix scale factor 2
    return (containerSize().height.toDouble() / 2).dp
}