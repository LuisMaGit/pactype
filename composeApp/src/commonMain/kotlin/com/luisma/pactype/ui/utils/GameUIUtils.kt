package com.luisma.pactype.ui.utils


data class CommonMeasurements(
    val mapTileSize: Int,
    val gameW: Int,
    val gameH: Int,
) {
    companion object {
        fun fromScreenSize(type: ScreenSizeBreakPoints): CommonMeasurements {
            return when (type) {
                ScreenSizeBreakPoints.Mobile -> CommonMeasurements(
                    mapTileSize = 10,
                    gameW = 340, // 10 * 34 = 340,
                    gameH = 310, // 10 * 31 = 310
                )

                ScreenSizeBreakPoints.Tablet -> CommonMeasurements(
                    mapTileSize = 20,
                    gameW = 680, // 20 * 34 = 680,
                    gameH = 620, // 20 * 31 = 620
                )

                ScreenSizeBreakPoints.TV -> CommonMeasurements(
                    mapTileSize = 30,
                    gameW = 1020, // 30 * 34 = 1020
                    gameH = 930, // 30 * 31 = 930
                )
            }
        }
    }
}


