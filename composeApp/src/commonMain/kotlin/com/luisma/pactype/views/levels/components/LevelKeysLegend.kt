package com.luisma.pactype.views.levels.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp
import org.jetbrains.compose.resources.stringResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.level_currently_playing
import pactype.composeapp.generated.resources.level_go_left
import pactype.composeapp.generated.resources.level_go_right
import pactype.composeapp.generated.resources.level_settings
import pactype.composeapp.generated.resources.normal_mode
import pactype.composeapp.generated.resources.practice_mode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LevelKeysLegend(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose
) {

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.padding(top = 16.dp)
    ) {
        LevelKeyLegend(
            char = 'f',
            text = stringResource(Res.string.level_go_left),
            levelTheme = levelTheme
        )
        LevelKeyLegend(
            char = 'j',
            text = stringResource(Res.string.level_go_right),
            levelTheme = levelTheme
        )
        LevelKeyLegend(
            char = 'n',
            text = stringResource(Res.string.normal_mode),
            levelTheme = levelTheme
        )
        LevelKeyLegend(
            char = 'p',
            text = stringResource(Res.string.practice_mode),
            levelTheme = levelTheme
        )
        LevelKeyLegend(
            char = 's',
            text = stringResource(Res.string.level_settings),
            levelTheme = levelTheme
        )
        LevelKeyLegend(
            char = 'g',
            text = stringResource(Res.string.level_currently_playing),
            levelTheme = levelTheme
        )
    }

}

@Composable
fun LevelKeyLegend(
    modifier: Modifier = Modifier,
    char: Char,
    text: String,
    levelTheme: LevelThemeCompose
) {

    Row(
        modifier = modifier.padding(end = 12.dp, bottom = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(end = 4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(levelTheme.textOff)
                .alignByBaseline(),
            contentAlignment = Alignment.Center
        ) {
            PText(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = char.toString(),
                fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
                color = levelTheme.background
            )
        }
        PText(
            modifier = Modifier.alignByBaseline(),
            text = text,
            fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
            color = levelTheme.textOff
        )
    }
}