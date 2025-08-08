package com.luisma.pactype.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import platform.UIKit.UIScreen


fun screenScale(): Double {
    return UIScreen.mainScreen.scale
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun containerSize(): IntSize {
    return LocalWindowInfo.current.containerSize
}

@Composable
actual fun getScreenWidth(): Dp =
    (containerSize().width.toDouble() / screenScale()).dp

@Composable
actual fun getScreenHeight(): Dp = (containerSize().height.toDouble() / screenScale()).dp