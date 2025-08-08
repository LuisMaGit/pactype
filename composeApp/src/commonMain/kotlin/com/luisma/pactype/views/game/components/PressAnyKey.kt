package com.luisma.pactype.views.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import com.luisma.pactype.ui.theme.P_FONT_SIZE_24
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp
import org.jetbrains.compose.resources.stringResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.game_press_any_key
import pactype.composeapp.generated.resources.new_best


@Composable
fun PressAnyKey(
    modifier: Modifier = Modifier,
    colorPressAny: Color,
    colorNewBest: Color,
    progress: Int,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PText(
                text = stringResource(Res.string.game_press_any_key),
                fontSize = P_FONT_SIZE_24.fontSizeNonScaledSp,
                color = colorPressAny
            )
            if (progress != 0) {
                Row {
                    PText(
                        text = "${stringResource(Res.string.new_best)} ",
                        fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
                        color = colorPressAny
                    )
                    PText(
                        text = "${progress}%",
                        fontSize = P_FONT_SIZE_12.fontSizeNonScaledSp,
                        color = colorNewBest
                    )
                }
            }
        }
    }
}