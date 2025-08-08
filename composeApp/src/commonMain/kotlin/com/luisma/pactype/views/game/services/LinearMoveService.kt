package com.luisma.pactype.views.game.services

import com.luisma.pactype.views.game.data.CELL_TYPE_PLAYER_CAN_MOVE
import com.luisma.pactype.views.game.data.Cell
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.data.MoveType
import com.luisma.pactype.views.game.data.PMap
import com.luisma.pactype.views.game.states.GameEnemyState
import com.luisma.pactype.views.game.states.PlayerState
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

class LinearMoveService {

    fun movePlayerToChar(
        player: PlayerState,
        char: Char,
        map: PMap
    ): PlayerState? {
        val cell = getPossibleCellsToMove(
            coordinate = player.coordinates, map = map
        ).firstOrNull { c -> c.char == char }

        // fail move
        if (cell == null) {
            return null
        }

        return PlayerState(
            coordinates = Coordinate(cell.x, cell.y),
            prevCoordinate = player.coordinates,
        )
    }

    fun moveEnemyToCell(
        cell: Cell,
        targetCoordinate: Coordinate,
        enemy: GameEnemyState
    ): GameEnemyState {
        return enemy.copy(
            coordinates = Coordinate(cell.x, cell.y),
            targetCoordinate = targetCoordinate,
            prevCoordinate = enemy.coordinates
        )
    }

    fun getPossibleCellsToMoveDiscardingOne(
        coordinate: Coordinate,
        coordinateToDiscard: Coordinate,
        map: PMap
    ): List<Cell> {
        val candidates = getPossibleCellsToMove(
            coordinate = coordinate, map = map
        ).toMutableList()

        val previousIdx = candidates.indexOfFirst { candidate ->
            candidate.x == coordinateToDiscard.first && candidate.y == coordinateToDiscard.second
        }

        if (previousIdx != -1) {
            candidates.removeAt(previousIdx)
        }

        return candidates
    }

    fun moveCoordinate(
        move: MoveType,
        x: Int,
        y: Int
    ): Coordinate {
        return when (move) {
            MoveType.Right -> Coordinate(x + 1, y)
            MoveType.Down -> Coordinate(x, y + 1)
            MoveType.Left -> Coordinate(x - 1, y)
            MoveType.Up -> Coordinate(x, y - 1)
        }
    }

    fun calculateDistance(
        point1: Coordinate,
        point2: Coordinate
    ): Int {
        // checks if are in the same axe, if are, get difference distance
        // if not, get distance from pythagoras formula,
        val distance = if (point1.first == point2.first) {
            (point1.second - point2.second).absoluteValue
        } else if (point1.second == point2.second) {
            (point1.first - point2.first).absoluteValue
        } else {
            val deltaX = (point1.first - point2.first).absoluteValue
            val deltaY = (point1.second - point2.second).absoluteValue
            sqrt(deltaX.toDouble().pow(2) + deltaY.toDouble().pow(2))
        }
        return distance.toInt()
    }

    fun getBestCellToMoveComparingDistance(
        candidates: List<Cell>,
        targetCoordinate: Coordinate
    ): Cell {
        // get distance from each candidate
        // get min distance
        val distances = mutableListOf<Int>()
        candidates.forEach { candidate ->
            val distance = calculateDistance(
                point1 = Coordinate(candidate.x, candidate.y), point2 = targetCoordinate
            )
            distances.add(distance)
        }
        val minDistance = distances.minOrNull()
        val cellIdx = distances.indexOf(minDistance)
        return candidates[cellIdx]
    }

    fun getMoveTypeFromPreviousAndCurrentCoordinates(
        currentCoordinate: Coordinate,
        previousCoordinate: Coordinate,
    ): MoveType {
        // x move
        if (currentCoordinate.second == previousCoordinate.second) {
            if (currentCoordinate.first - previousCoordinate.first == 1) {
                return MoveType.Right
            }
            if (currentCoordinate.first - previousCoordinate.first == -1) {
                return MoveType.Left
            }
        }
        // y move
        if (currentCoordinate.first == previousCoordinate.first) {
            if (currentCoordinate.second - previousCoordinate.second == 1) {
                return MoveType.Down
            }
            if (currentCoordinate.second - previousCoordinate.second == -1) {
                return MoveType.Up
            }
        }
        // default player move
        return MoveType.Right
    }

    private fun getPossibleCellsToMove(
        coordinate: Coordinate,
        map: PMap
    ): List<Cell> {
        val output = mutableListOf<Cell>()
        for (move in MoveType.entries) {
            val candidate = moveCoordinate(
                move = move, x = coordinate.first, y = coordinate.second
            )
            val cell = map[candidate]!!
            if (CELL_TYPE_PLAYER_CAN_MOVE.contains(cell.type)) {
                output.add(cell)
            }
        }
        return output
    }


}
