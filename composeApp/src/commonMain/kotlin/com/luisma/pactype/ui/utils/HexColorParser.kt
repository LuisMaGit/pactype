package com.luisma.pactype.ui.utils

import androidx.compose.ui.graphics.Color


fun Color.Companion.parseColor(colorHEX: String): Color {

    return when (colorHEX.length) {
        3 -> {
            // RGB -> RRGGBB
            val r = colorHEX[0].digitToInt(16) * 17
            val g = colorHEX[1].digitToInt(16) * 17
            val b = colorHEX[2].digitToInt(16) * 17
            Color(red = r, green = g, blue = b, alpha = 255)
        }

        4 -> {
            // ARGB
            val a = colorHEX[0].digitToInt(radix = 16) * 17
            val r = colorHEX[1].digitToInt(16) * 17
            val g = colorHEX[2].digitToInt(16) * 17
            val b = colorHEX[3].digitToInt(16) * 17
            Color(alpha = a, red = r, green = g, blue = b)
        }

        6 -> {
            // RRGGBB
            val r = colorHEX.substring(0, 2).toInt(16)
            val g = colorHEX.substring(2, 4).toInt(16)
            val b = colorHEX.substring(4, 6).toInt(16)
            Color(red = r, green = g, blue = b, alpha = 255)
        }

        8 -> {
            // AARRGGBB
            val a = colorHEX.substring(0, 2).toInt(16)
            val r = colorHEX.substring(2, 4).toInt(16)
            val g = colorHEX.substring(4, 6).toInt(16)
            val b = colorHEX.substring(6, 8).toInt(16)
            Color(alpha = a, red = r, green = g, blue = b)
        }

        else -> throw IllegalArgumentException("Invalid Hex color: $colorHEX")
    }
}