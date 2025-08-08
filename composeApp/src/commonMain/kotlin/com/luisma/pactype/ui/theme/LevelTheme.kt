package com.luisma.pactype.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.luisma.pactype.ui.utils.parseColor


@Immutable
data class LevelTheme(
    val main: String,
    val background: String,
    val textOn: String,
    val textOff: String,
    val key: String,
) {
    companion object {
        fun initial(): LevelTheme {
            return LevelTheme(
                main = DefaultColors.MAIN_HEX,
                background = DefaultColors.BACKGROUND_HEX,
                textOn = DefaultColors.TEXT_ON_HEX,
                textOff = DefaultColors.TEXT_OFF_HEX,
                key = DefaultColors.KEY_HEX
            )
        }
    }
}

data class LevelThemeCompose(
    val main: Color,
    val background: Color,
    val textOn: Color,
    val textOff: Color,
    val key: Color,
)

fun levelThemeConverter(levelTheme: LevelTheme): LevelThemeCompose {
    return LevelThemeCompose(
        main = Color.parseColor(levelTheme.main),
        background = Color.parseColor(levelTheme.background),
        textOn = Color.parseColor(levelTheme.textOn),
        textOff = Color.parseColor(levelTheme.textOff),
        key = Color.parseColor(levelTheme.key),
    )
}