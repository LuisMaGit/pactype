package com.luisma.pactype.views.levels.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.theme.LevelThemeCompose
import org.jetbrains.compose.resources.stringResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.level_go_to_latest

@Composable
fun LevelBottomBanner(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(levelTheme.main),
        contentAlignment = Alignment.Center
    ) {
        PText(
            text = stringResource(Res.string.level_go_to_latest),
            color = levelTheme.background,
        )
    }
}