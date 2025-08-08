package com.luisma.pactype.views.game.data

enum class EnemyType(val dbOrdinal: Int) {
    Blue(dbOrdinal = 0),
    Green(dbOrdinal = 1),
    Red(dbOrdinal = 2),
    Yellow(dbOrdinal = 3);

    companion object {
        fun getTypeByDBOrdinal(dbOrdinal: Int): EnemyType {
            return EnemyType.entries.first { it.dbOrdinal == dbOrdinal }
        }
    }
}

enum class EnemyMode {
    WaitingInHome,
    MovingInHome,
    Chasing
}

const val YELLOW_TARGET_SWITCH_TARGET_TILES_AMOUNT = 8
const val GREEN_TARGET_TILES_AHEAD_AMOUNT = 2
const val RED_TARGET_TILES_AHEAD_AMOUNT = 4
val MAPS_BOTTOM_LEFT_COORDINATE = Coordinate(1, 29)
const val LOST_SHAKE_ANIMATION_DURATION_MS = 500