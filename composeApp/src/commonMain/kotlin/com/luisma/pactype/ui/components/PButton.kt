package com.luisma.pactype.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12


@Composable
fun PButton(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose,
    enabled: Boolean,
    text: String,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = if (enabled) levelTheme.main else levelTheme.key)
            .padding(vertical = 4.dp, horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        PText(
            text = text,
            fontSize = P_FONT_SIZE_12.sp,
            color = levelTheme.textOn
        )
    }
}