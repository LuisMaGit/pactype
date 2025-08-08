package com.luisma.pactype.views.game

import androidx.compose.ui.graphics.Color
import com.luisma.pactype.ui.theme.DefaultColors
import com.luisma.pactype.ui.utils.CommonMeasurements
import com.luisma.pactype.ui.utils.ScreenSizeBreakPoints
import com.luisma.pactype.views.game.data.EnemyType

data class GameUIMeasurements (
    val commonMeasurements: CommonMeasurements,
    val fontSize: Int,
    val enemyEyeSize: Int,
    val keyWidth: Int,
    val keyHeight: Int,
    val keyFont: Int,
) {
    companion object {
        fun fromScreenSize(type: ScreenSizeBreakPoints): GameUIMeasurements {
            val commonMeasurements = CommonMeasurements.fromScreenSize(type)
            return when (type) {
                ScreenSizeBreakPoints.Mobile -> GameUIMeasurements(
                    commonMeasurements = commonMeasurements,
                    fontSize = 8,
                    enemyEyeSize = 2,
                    keyWidth = 30,
                    keyHeight = 58,
                    keyFont = 24,
                )

                ScreenSizeBreakPoints.Tablet -> GameUIMeasurements(
                    commonMeasurements = commonMeasurements,
                    fontSize = 16,
                    enemyEyeSize = 4,
                    keyWidth = 64,
                    keyHeight = 64,
                    keyFont = 48,
                )

                ScreenSizeBreakPoints.TV -> GameUIMeasurements(
                    commonMeasurements = commonMeasurements,
                    fontSize = 24,
                    enemyEyeSize = 6,
                    keyWidth = 98,
                    keyHeight = 98,
                    keyFont = 88,
                )
            }
        }
    }
}


fun enemyColorByType(enemyType: EnemyType): Color {
    return when (enemyType) {
        EnemyType.Blue -> DefaultColors.enemyBlue
        EnemyType.Green -> DefaultColors.enemyGreen
        EnemyType.Red -> DefaultColors.enemyRed
        EnemyType.Yellow -> DefaultColors.enemyYellow
    }
}