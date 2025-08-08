package com.luisma.pactype.views.levels.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.components.ProgressBar
import com.luisma.pactype.ui.components.Timer
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import com.luisma.pactype.ui.theme.P_FONT_SIZE_20
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.best_time
import pactype.composeapp.generated.resources.ic_flag
import pactype.composeapp.generated.resources.ic_play_controller
import pactype.composeapp.generated.resources.normal_mode
import pactype.composeapp.generated.resources.practice_mode

val LEVEL_CARD_W = 300.dp
val LEVEL_CARD_H = 120.dp

@Composable
fun LevelCard(
    modifier: Modifier = Modifier,
    theme: LevelThemeCompose,
    practiceMode: Boolean,
    bestTimeCS: Int?,
    progress: Int?,
) {
    Box(
        modifier = modifier
            .height(LEVEL_CARD_H)
            .width(LEVEL_CARD_W)
            .border(
                color = theme.textOn,
                width = 2.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1F)
                    .size(46.dp),
                painter = painterResource(
                    if (practiceMode) {
                        Res.drawable.ic_flag
                    } else {
                        Res.drawable.ic_play_controller
                    }
                ),
                colorFilter = ColorFilter.tint(color = theme.textOn),
                contentDescription = ""
            )
            Column(
                modifier = Modifier.weight(2F),
            ) {
                PText(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = if (practiceMode) {
                        stringResource(Res.string.practice_mode)
                    } else {
                        stringResource(Res.string.normal_mode)
                    },
                    fontSize = P_FONT_SIZE_20.fontSizeNonScaledSp,
                    color = theme.textOn
                )
                if (progress != null) {
                    ProgressBar(
                        modifier = Modifier.padding(bottom = 8.dp),
                        width = 180,
                        height = 15,
                        completedColor = theme.main,
                        unCompletedColor = theme.key,
                        textColor = theme.textOn,
                        progress = progress
                    )
                }
                if (bestTimeCS != null && bestTimeCS != 0) {
                    Row {
                        PText(
                            modifier = Modifier.alignByBaseline(),
                            text = stringResource(Res.string.best_time) + "\t",
                            fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
                            color = theme.textOn
                        )
                        Timer(
                            modifier = Modifier.alignByBaseline(),
                            timeCS = bestTimeCS,
                            color = theme.main,
                            unitColor = theme.main
                        )
                    }

                }
            }
        }
    }
}