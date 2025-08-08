package com.luisma.pactype.ui.components


import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.luisma.pactype.ui.theme.DefaultColors
import com.luisma.pactype.ui.theme.P_FONT_SIZE_16
import com.luisma.pactype.ui.theme.getRobotoFontFamily
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp

@Composable
fun PText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = P_FONT_SIZE_16.fontSizeNonScaledSp,
    color: Color = DefaultColors.textOn,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    fontFamily: FontFamily = getRobotoFontFamily(),
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight : TextUnit = TextUnit.Unspecified
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        overflow = overflow,
        style = TextStyle(
            color = color,
            fontSize = fontSize,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight
        )
    )
}