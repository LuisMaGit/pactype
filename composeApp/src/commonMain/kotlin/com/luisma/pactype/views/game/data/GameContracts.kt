package com.luisma.pactype.views.game.data

import kotlinx.collections.immutable.ImmutableMap


const val DISABLED_CHAR = '*'
const val ENEMY_HOME_CHAR = '='
const val SPACE_CHAR = '␣'
const val X_TILE_COUNT = 34
const val Y_TILE_COUNT = 31
val NON_LETTERS_SUPPORTED = setOf(',', '.', ' ')
val CELL_TYPE_PLAYER_CAN_MOVE = setOf(CellType.Letter, CellType.Space)
val PLAYER_START_COORDINATE = Coordinate(16, 17)

typealias X = Int
typealias Y = Int
typealias MapType = Int // 1..4
typealias Coordinate = Pair<X, Y>
typealias PMap = ImmutableMap<Coordinate, Cell>


data class GameContractsByMapType(
    val mapType: MapType,
    val firstEnemyStartCoordinate: Coordinate,
) {
    val enemyHomeBreakCoordinate: Coordinate
        get() = Coordinate(firstEnemyStartCoordinate.first + 1, firstEnemyStartCoordinate.second)

    companion object {
        fun initial(): GameContractsByMapType {
            return GameContractsByMapType(
                mapType = 0,
                firstEnemyStartCoordinate = Coordinate(-1, -1),
            )
        }
    }
}

fun gameContractsByMapType(mapType: MapType): GameContractsByMapType {
    return when (mapType) {
        1, 2, 3, 5 -> GameContractsByMapType(
            mapType = mapType,
            firstEnemyStartCoordinate = Coordinate(15, 14),
        )

        4 -> GameContractsByMapType(
            mapType = mapType,
            firstEnemyStartCoordinate = Coordinate(15, 13),
        )

        else -> GameContractsByMapType.initial()
    }
}
