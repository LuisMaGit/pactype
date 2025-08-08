package com.luisma.pactype.views.game.services.enemies_services

import com.luisma.pactype.views.game.data.CELL_TYPE_PLAYER_CAN_MOVE
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.data.EnemyType
import com.luisma.pactype.views.game.data.GREEN_TARGET_TILES_AHEAD_AMOUNT
import com.luisma.pactype.views.game.data.MAPS_BOTTOM_LEFT_COORDINATE
import com.luisma.pactype.views.game.data.PMap
import com.luisma.pactype.views.game.data.RED_TARGET_TILES_AHEAD_AMOUNT
import com.luisma.pactype.views.game.data.YELLOW_TARGET_SWITCH_TARGET_TILES_AMOUNT
import com.luisma.pactype.views.game.services.LinearMoveService
import com.luisma.pactype.views.game.states.GameEnemyState

class EnemyTargetCoordinateService(
    val linearMoveService: LinearMoveService
) {

    fun getTargetCoordinate(
        map: PMap,
        enemy: GameEnemyState,
        playerCoordinate: Coordinate,
        playerPreviousCoordinate: Coordinate,
    ): Coordinate {
        return when (enemy.enemyType) {
            EnemyType.Blue -> playerCoordinate
            EnemyType.Green -> getAheadTargetCoordinate(
                map = map,
                playerCoordinate = playerCoordinate,
                playerPreviousCoordinate = playerPreviousCoordinate,
                numbTilesAhead = GREEN_TARGET_TILES_AHEAD_AMOUNT
            )

            EnemyType.Red -> getAheadTargetCoordinate(
                map = map,
                playerCoordinate = playerCoordinate,
                playerPreviousCoordinate = playerPreviousCoordinate,
                numbTilesAhead = RED_TARGET_TILES_AHEAD_AMOUNT
            )

            EnemyType.Yellow -> getYellowTargetCoordinate(
                playerCoordinate = playerCoordinate,
                enemyCoordinate = enemy.coordinates
            )
        }
    }

    private fun getAheadTargetCoordinate(
        map: PMap,
        playerCoordinate: Coordinate,
        playerPreviousCoordinate: Coordinate,
        numbTilesAhead: Int
    ): Coordinate {
        // get from where the player is coming [MoveType]
        // from this MoveType add the amount of tiles ahead
        // if the tiles don't fit put the target in the last fittable coordinate
        val move = linearMoveService.getMoveTypeFromPreviousAndCurrentCoordinates(
            currentCoordinate = playerCoordinate,
            previousCoordinate = playerPreviousCoordinate
        )
        var output = playerCoordinate
        for (idx in 0 until numbTilesAhead) {
            val savedPrev = output
            output = linearMoveService.moveCoordinate(
                move = move,
                x = output.first,
                y = output.second
            )
            if (!CELL_TYPE_PLAYER_CAN_MOVE.contains(map[output]?.type)) {
                output = savedPrev
                break
            }
        }
        return output
    }

    private fun getYellowTargetCoordinate(
        playerCoordinate: Coordinate,
        enemyCoordinate: Coordinate,
    ): Coordinate {
        // get distance between the player and the enemy
        // less than [YELLOW_TARGET_SWITCH_TARGET_TILES_AMOUNT] tiles -> target tile left corner [MAPS_LEFT_COORDINATE_COORDINATE]
        // more than [YELLOW_TARGET_SWITCH_TARGET_TILES_AMOUNT] tiles -> player coordinate
        val distance = linearMoveService.calculateDistance(
            point1 = playerCoordinate,
            point2 = enemyCoordinate
        )
        if (distance < YELLOW_TARGET_SWITCH_TARGET_TILES_AMOUNT) {
            return MAPS_BOTTOM_LEFT_COORDINATE
        }
        return playerCoordinate
    }
}