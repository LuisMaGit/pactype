package com.luisma.pactype.views.game_dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.CIRCLE_ICON_BTN_BIG_ICON_SIZE
import com.luisma.pactype.ui.components.CIRCLE_ICON_BTN_BIG_SIZE
import com.luisma.pactype.ui.components.CircleIconButton
import com.luisma.pactype.ui.components.DialogWrapper
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.components.ProgressBar
import com.luisma.pactype.ui.components.Timer
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp
import org.jetbrains.compose.resources.stringResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.game_dialog_level_complete
import pactype.composeapp.generated.resources.ic_again
import pactype.composeapp.generated.resources.ic_menu
import pactype.composeapp.generated.resources.ic_play
import pactype.composeapp.generated.resources.new_best
import pactype.composeapp.generated.resources.normal_mode
import pactype.composeapp.generated.resources.practice_mode
import pactype.composeapp.generated.resources.time


enum class GameDialogType {
    Pause,
    Win
}

@Composable
fun GameDialog(
    modifier: Modifier = Modifier,
    type: GameDialogType,
    levelTheme: LevelThemeCompose,
    levelName: String,
    practiceMode: Boolean,
    progress: Int,
    timeCS: Int,
    showNewBest: Boolean,
    onResume: () -> Unit,
    onReset: () -> Unit,
    goToMenu: () -> Unit,
) {

    @Composable
    fun title(): String {
        return when (type) {
            GameDialogType.Pause -> levelName
            GameDialogType.Win -> stringResource(Res.string.game_dialog_level_complete)
        }
    }

    fun titleColor(): Color {
        return when (type) {
            GameDialogType.Pause -> levelTheme.textOn
            GameDialogType.Win -> levelTheme.main
        }
    }

    DialogWrapper(
        modifier = modifier,
        color = levelTheme.background,
        borderColor = levelTheme.textOn,
        title = title(),
        titleColor = titleColor()
    ) {
        // game mode
        PText(
            text = if (practiceMode) {
                stringResource(Res.string.practice_mode)
            } else stringResource(
                Res.string.normal_mode
            ),
            color = levelTheme.textOn,
            fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
            maxLines = 1,
        )
        // bar
        ProgressBar(
            modifier = Modifier.padding(top = 4.dp),
            progress = progress,
            completedColor = levelTheme.main,
            unCompletedColor = levelTheme.key,
            textColor = levelTheme.textOn,
        )
        // timer
        Row(
            modifier = modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            PText(
                modifier = Modifier.alignByBaseline(),
                text = stringResource(Res.string.time) + "\t",
                fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
                color = levelTheme.textOn
            )
            Timer(
                modifier = Modifier.alignByBaseline(),
                timeCS = timeCS,
                color = levelTheme.main,
                unitColor = levelTheme.textOn
            )
            if (showNewBest)
                PText(
                    modifier = Modifier.alignByBaseline(),
                    text = "\t" + stringResource(Res.string.new_best),
                    fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
                    color = levelTheme.main
                )
        }
        // buttons
        GameDialogButtons(
            modifier = modifier.padding(top = 24.dp),
            type = type,
            color = levelTheme.key,
            iconColor = levelTheme.main,
            onResume = onResume,
            onReset = onReset,
            goToMenu = goToMenu,
        )
    }

}


@Composable
fun GameDialogButtons(
    modifier: Modifier,
    type: GameDialogType,
    color: Color,
    iconColor: Color,
    onResume: () -> Unit,
    onReset: () -> Unit,
    goToMenu: () -> Unit,
) {

    when (type) {
        GameDialogType.Pause -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(.5F))
                // replay
                CircleIconButton(
                    modifier = Modifier.clickable {
                        onReset()
                    },
                    color = color,
                    colorIcon = iconColor,
                    icon = Res.drawable.ic_again,
                )
                Spacer(modifier = Modifier.weight(.5F))
                // play
                CircleIconButton(
                    modifier = Modifier.clickable {
                        onResume()
                    },
                    color = color,
                    colorIcon = iconColor,
                    icon = Res.drawable.ic_play,
                    size = CIRCLE_ICON_BTN_BIG_SIZE,
                    sizeIcon = CIRCLE_ICON_BTN_BIG_ICON_SIZE,
                )
                Spacer(modifier = Modifier.weight(.5F))
                // menu
                CircleIconButton(
                    modifier = Modifier.clickable {
                        goToMenu()
                    },
                    color = color,
                    colorIcon = iconColor,
                    icon = Res.drawable.ic_menu,
                )
                Spacer(modifier = Modifier.weight(.5F))
            }
        }

        GameDialogType.Win -> Row(
            modifier = modifier,
        ) {
            Spacer(modifier = Modifier.weight(.5F))
            // replay
            CircleIconButton(
                modifier = Modifier.clickable {
                    onReset()
                },
                color = color,
                colorIcon = iconColor,
                icon = Res.drawable.ic_again,
                size = CIRCLE_ICON_BTN_BIG_SIZE,
                sizeIcon = CIRCLE_ICON_BTN_BIG_ICON_SIZE,
            )
            Spacer(modifier = Modifier.weight(.5F))
            // menu
            CircleIconButton(
                modifier = Modifier.clickable {
                    goToMenu()
                },
                color = color,
                colorIcon = iconColor,
                icon = Res.drawable.ic_menu,
                size = CIRCLE_ICON_BTN_BIG_SIZE,
                sizeIcon = CIRCLE_ICON_BTN_BIG_ICON_SIZE,
            )
            Spacer(modifier = Modifier.weight(.5F))
        }
    }

}