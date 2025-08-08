package com.luisma.pactype.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.RobotoMono_Bold
import pactype.composeapp.generated.resources.RobotoMono_Regular

const val P_FONT_SIZE_12 = 12
const val P_FONT_SIZE_16 = 16
const val P_FONT_SIZE_20 = 20
const val P_FONT_SIZE_24 = 24
const val P_FONT_SIZE_32 = 32
const val P_FONT_SIZE_36 = 36

@Composable
fun getRobotoFontFamily(): FontFamily {
    return FontFamily(
        Font(resource = Res.font.RobotoMono_Bold, weight = FontWeight.Bold),
        Font(resource = Res.font.RobotoMono_Regular, weight = FontWeight.Normal)
    )
}
