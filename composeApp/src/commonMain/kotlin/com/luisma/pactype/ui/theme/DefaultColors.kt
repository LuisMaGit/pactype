package com.luisma.pactype.ui.theme

import androidx.compose.ui.graphics.Color
import com.luisma.pactype.ui.utils.parseColor

object DefaultColors {
    const val MAIN_HEX = "E2B714"
    val main = Color.parseColor("E2B714")

    const val BACKGROUND_HEX = "323437"
    val background = Color.parseColor(BACKGROUND_HEX)

    const val KEY_HEX = "2B2E31"
    val key = Color.parseColor(KEY_HEX)

    const val TEXT_ON_HEX = "D1D0C5"
    val textOn = Color.parseColor(TEXT_ON_HEX)

    const val TEXT_OFF_HEX = "636669"
    val textOff = Color.parseColor(TEXT_OFF_HEX)

    private const val ENEMY_FEATURES_HEX = "FFFFFF"
    val enemyFeatures = Color.parseColor(ENEMY_FEATURES_HEX)

    private const val ENEMY_BLUE_HEX = "4DC1FF"
    val enemyBlue = Color.parseColor(ENEMY_BLUE_HEX)

    private const val ENEMY_GREEN_HEX = "8DFF9B"
    val enemyGreen = Color.parseColor(ENEMY_GREEN_HEX)

    private const val ENEMY_RED_HEX = "FF9193"
    val enemyRed = Color.parseColor(ENEMY_RED_HEX)

    private const val ENEMY_YELLOW_HEX = "FFCF11"
    val enemyYellow = Color.parseColor(ENEMY_YELLOW_HEX)
}