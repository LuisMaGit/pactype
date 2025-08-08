package com.luisma.pactype.views.levels.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_36
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp
import com.luisma.pactype.views.levels.states.LevelsLogoState


const val LEVEL_CARET_W = 18
const val LEVEL_CARET_H = 47

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose,
    logoState: LevelsLogoState,
) {

    val offsetX by animateDpAsState(
        targetValue = if (logoState.caretOffset != logoState.caretOffsetPrev) {
            logoState.caretOffset.dp
        } else {
            logoState.caretOffsetPrev.dp
        },
        animationSpec = tween(
            durationMillis = 2 * logoState.charToggleDelayMS,
            easing = LinearOutSlowInEasing
        )
    )

    Box(
        modifier = modifier,
    ) {
        for ((idx, char) in logoState.chars.withIndex()) {
            val width = logoState.charWidth
            val previousCharWidth = if (idx == 0) 0 else width
            LogoChar(
                modifier = Modifier
                    .width(width.dp)
                    .offset(previousCharWidth.dp * idx),
                color = if (char.done) levelTheme.textOff else levelTheme.textOn,
                char = char.char
            )
        }
        Box(
            modifier = Modifier
                .offset(
                    x = if (logoState.animationDone) {
                        logoState.caretOffset.dp
                    } else {
                        offsetX
                    }
                )
                .width(LEVEL_CARET_W.dp)
                .height(LEVEL_CARET_H.dp)
                .background(levelTheme.main)
        )
    }
}

@Composable
private fun LogoChar(
    modifier: Modifier = Modifier,
    color: Color,
    char: Char,
) {
    PText(
        modifier = modifier,
        text = char.toString(),
        fontSize = P_FONT_SIZE_36.fontSizeNonScaledSp,
        color = color,
        overflow = TextOverflow.Visible
    )
}

