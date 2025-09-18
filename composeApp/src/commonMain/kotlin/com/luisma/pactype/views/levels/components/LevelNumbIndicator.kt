package com.luisma.pactype.views.levels.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_16
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp

val LEVEL_NUMB_INDICATOR_H = 36.dp

@Composable
fun LevelNumbIndicator(
    modifier: Modifier = Modifier,
    level: Int,
    total: Int,
    levelTheme: LevelThemeCompose
) {
    Row(
        modifier = modifier.height(LEVEL_NUMB_INDICATOR_H),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PText(
            text = level.toString(),
            fontSize = P_FONT_SIZE_16.fontSizeNonScaledSp,
            color = levelTheme.textOn
        )
        PText(
            text = " / $total",
            fontSize = P_FONT_SIZE_16.fontSizeNonScaledSp,
            color = levelTheme.textOff
        )
    }
}