package com.luisma.pactype.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.luisma.pactype.ui.theme.DefaultColors
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import com.luisma.pactype.ui.theme.getRobotoFontFamily

@Composable
fun BulletPointText(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = P_FONT_SIZE_12.sp,
    colorBullet: Color = DefaultColors.main,
    colorText : Color = DefaultColors.textOn,
    text: String
) {

    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = getRobotoFontFamily(),
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colorBullet
                )
            ) {
                append("- ")
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = getRobotoFontFamily(),
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colorText
                )
            ) {
                append(text)
            }
        }
    )
}