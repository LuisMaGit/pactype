package com.luisma.pactype.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp


const val TIMER_H = 20

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    timeCS: Int,
    color: Color,
    unitColor: Color,
) {

    val totalMS = timeCS * 10
    val minutes = totalMS / 60_000
    val afterMinutes = totalMS % 60_000
    val seconds = afterMinutes / 1000
    val ms = afterMinutes % 1000

    val fMin = if (minutes < 10) "0$minutes" else minutes.toString()
    val fSec = if (seconds < 10) "0$seconds" else seconds.toString()
    val fMs = if (ms < 10) "00$ms" else if (ms < 99) "0$ms" else ms.toString()

    Row(
        modifier = modifier.height(height = TIMER_H.dp)
    ) {
        TimerUnit(
            unit = fMin,
            color = color,
        )
        TimerUnit(
            unit = "m",
            color = unitColor,
        )
        TimerUnit(
            unit = fSec,
            color = color,
        )
        TimerUnit(
            unit = "s",
            color = unitColor,
        )
        TimerUnit(
            unit = fMs,
            color = color,
        )
        TimerUnit(
            unit = "ms",
            color = unitColor,
        )
    }
}


@Composable
fun TimerUnit(
    unit: String,
    color: Color
) {
    PText(
        text = unit,
        fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
        color = color
    )
}
