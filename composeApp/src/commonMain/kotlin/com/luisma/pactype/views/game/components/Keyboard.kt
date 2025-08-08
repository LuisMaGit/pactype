package com.luisma.pactype.views.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp
import com.luisma.pactype.views.game.data.PKey
import com.luisma.pactype.views.game.data.PKeyType
import com.luisma.pactype.views.game.data.PKeyboard

val KEYBOARD_H_PADDING = 4.dp
val KEYBOARD_V_PADDING = 8.dp
val KEYBOARD_MARGIN_BOTTOM = 24.dp

@Composable
fun Keyboard(
    modifier: Modifier = Modifier,
    keyW: Int,
    keyH: Int,
    capital: Boolean,
    colorKey: Color,
    colorLetter: Color,
    fontSize: Int,
    onTap: (keyType: PKey) -> Unit,
) {
    val keyboard = PKeyboard.fullKeyboard()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // qwerty
        Row(
            modifier = Modifier.padding(bottom = KEYBOARD_V_PADDING)
        ) {
            keyboard.row1.forEachIndexed { idx, key ->
                val paddingEnd = if (idx != keyboard.row1.size - 1) KEYBOARD_H_PADDING else 0.dp
                KeyboardKey(
                    modifier = Modifier.padding(end = paddingEnd),
                    color = colorKey,
                    colorText = colorLetter,
                    width = keyW,
                    height = keyH,
                    onTap = onTap,
                    key = key,
                    capital = capital,
                    fontSize = fontSize,
                )
            }
        }
        // asdf
        Row(
            modifier = Modifier.padding(bottom = KEYBOARD_V_PADDING)
        ) {
            keyboard.row2.forEachIndexed { idx, key ->
                val paddingEnd = if (idx != keyboard.row2.size - 1) KEYBOARD_H_PADDING else 0.dp
                KeyboardKey(
                    modifier = Modifier.padding(end = paddingEnd),
                    color = colorKey,
                    colorText = colorLetter,
                    width = keyW,
                    height = keyH,
                    onTap = onTap,
                    key = key,
                    capital = capital,
                    fontSize = fontSize,
                )
            }
        }
        // ⇧zxcv⇧
        Row(
            modifier = Modifier.padding(bottom = KEYBOARD_V_PADDING)
        ) {
            keyboard.row3.forEachIndexed { idx, key ->
                val paddingEnd = if (idx != keyboard.row3.size - 1) KEYBOARD_H_PADDING else 0.dp
                val keyWidth = if (key.type == PKeyType.Shift) {
                    keyW + (keyW / 2)
                } else {
                    keyW
                }
                KeyboardKey(
                    modifier = Modifier.padding(end = paddingEnd),
                    color = colorKey,
                    colorText = colorLetter,
                    width = keyWidth,
                    height = keyH,
                    onTap = onTap,
                    key = key,
                    capital = capital,
                    fontSize = fontSize,
                )
            }
        }
        // ,␣.
        Row {
            keyboard.row4.forEachIndexed { idx, key ->
                val paddingEnd = if (idx != keyboard.row3.size - 1) KEYBOARD_H_PADDING else 0.dp
                val keyWidth = if (key.type == PKeyType.Dot || key.type == PKeyType.Comma) {
                    2 * keyW
                } else {
                    5 * keyW
                }
                KeyboardKey(
                    modifier = Modifier.padding(end = paddingEnd),
                    color = colorKey,
                    colorText = colorLetter,
                    width = keyWidth,
                    height = keyH,
                    onTap = onTap,
                    key = key,
                    capital = capital,
                    fontSize = fontSize,
                )
            }
        }
    }

}


@Composable
fun KeyboardKey(
    modifier: Modifier,
    key: PKey,
    onTap: (key: PKey) -> Unit,
    capital: Boolean,
    width: Int,
    height: Int,
    color: Color,
    colorText: Color,
    fontSize: Int,
    borderR: Int = 4,
) {
    Box(
        modifier = modifier
            .clickable {
                onTap(key)
            }
            .width(width.dp)
            .height(height.dp)
            .background(
                color,
                shape = RoundedCornerShape(size = borderR.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        val char = if (capital) key.capital else key.lower
        PText(
            text = char.toString(),
            color = colorText,
            fontSize = fontSize.fontSizeNonScaledSp
        )
    }
}