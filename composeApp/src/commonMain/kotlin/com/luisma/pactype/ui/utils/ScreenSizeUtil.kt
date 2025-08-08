package com.luisma.pactype.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luisma.pactype.platform.getScreenHeight
import com.luisma.pactype.platform.getScreenWidth

enum class ScreenSizeBreakPoints {
    Mobile,
    Tablet,
    TV,
}

@Composable
fun ScreenSizeUtil(
    content: @Composable (
        breakPoint: ScreenSizeBreakPoints,
        screenWidthDp: Dp,
        screenHeightDp: Dp
    ) -> Unit
) {
    val screenWidthDp = getScreenWidth()
    val screenHeightDp = getScreenHeight()

    val typeW = when (screenWidthDp) {
        in 0.dp..720.dp -> ScreenSizeBreakPoints.Mobile
        in 721.dp..<1920.dp -> ScreenSizeBreakPoints.Tablet
        else -> ScreenSizeBreakPoints.TV
    }

    val typeH = when (screenHeightDp) {
        in 0.dp..750.dp -> ScreenSizeBreakPoints.Mobile
        in 751.dp..<1100.dp -> ScreenSizeBreakPoints.Tablet
        else -> ScreenSizeBreakPoints.TV
    }
    val minorType = if (typeW.ordinal <= typeH.ordinal) typeW else typeH

    content(minorType, screenWidthDp, screenHeightDp)
}