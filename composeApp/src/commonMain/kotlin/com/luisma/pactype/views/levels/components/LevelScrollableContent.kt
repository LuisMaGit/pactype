package com.luisma.pactype.views.levels.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_32
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp
import com.luisma.pactype.views.levels.states.LevelState


val LEVEL_CARD_SPACE_BETWEEN = 60.dp
val LEVEL_NAME_H = 50.dp

@Composable
fun LevelScrollableContent(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose,
    level: LevelState,
    goToLevel: (levelId: Int, practiceMode: Boolean) -> Unit,
    onScreen: (levelId: Int) -> Unit,
) {

    LaunchedEffect(key1 = level.levelId) {
        onScreen(level.levelId)
    }

    Column(
        modifier = modifier,
    ) {
        LevelCard(
            modifier = Modifier
                .padding(bottom = LEVEL_CARD_SPACE_BETWEEN)
                .clickable {
                    goToLevel(level.levelId, false)
                },
            theme = levelTheme,
            practiceMode = false,
            bestTimeCS = level.playModeBestTimeCS,
            progress = level.playModeProgress,
        )
        LevelCard(
            modifier = Modifier
                .padding(bottom = LEVEL_CARD_SPACE_BETWEEN)
                .clickable {
                    goToLevel(level.levelId, true)
                },
            theme = levelTheme,
            practiceMode = true,
            bestTimeCS = level.practiceModeBestTimeCS,
            progress = null
        )
        PText(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .height(LEVEL_NAME_H),
            text = level.levelName,
            fontSize = P_FONT_SIZE_32.fontSizeNonScaledSp,
            color = levelTheme.textOn,
            maxLines = 1
        )
    }
}