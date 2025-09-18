package com.luisma.pactype.views.game.services

import com.luisma.pactype.views.game.data.CELL_TYPE_PLAYER_CAN_MOVE
import com.luisma.pactype.views.game.data.Cell
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.data.EnemyMode
import com.luisma.pactype.views.game.data.MoveType
import com.luisma.pactype.views.game.data.PMap
import com.luisma.pactype.views.game.data.X_TILE_COUNT
import com.luisma.pactype.views.game.data.Y_TILE_COUNT
import com.luisma.pactype.views.game.states.GameEnemyState
import com.luisma.pactype.views.game.states.PlayerState
import kotlinx.collections.immutable.ImmutableList


const val ROBOT_NEAR_TILES_TO_CHECK_AMOUNT = 5
const val ROBOT_MOVE_DELAY_MS = 333L
const val ROBOT_RUN_FROM_ENEMY_TILES_AMOUNT = 4

class RobotPlayerService(
    private val linearMoveService: LinearMoveService
) {

    fun movePlayer(
        map: PMap,
        player: PlayerState,
        enemies: ImmutableList<GameEnemyState>
    ): RobotPlayerMoveResponse? {

        var nextMove = movePlayerToProgress(
            map = map,
            playerCoordinate = player.coordinates,
            playerPreviousCoordinate = player.prevCoordinate,
        )

        if (nextMove != null && checkIfEnemyIsApproaching(
                map = map,
                playerCurrentCoordinate = player.coordinates,
                playerNextMove = nextMove.coordinates,
                enemies = enemies
            )
        ) {
            nextMove = movePlayerToRunFromEnemy(
                map = map,
                playerCoordinate = player.coordinates,
                moveToDiscard = nextMove.coordinates,
            ) ?: nextMove
        }

        return nextMove
    }

    private fun movePlayerToProgress(
        map: PMap,
        playerCoordinate: Coordinate,
        playerPreviousCoordinate: Coordinate,
    ): RobotPlayerMoveResponse? {

        // get possible moves (with previous move discarded)
        // if it is none, go back
        // if it is only one, move there
        // more than one, get target from [getNearTargetToMove] or [getFarTargetToMove]

        val candidates = linearMoveService.getPossibleCellsToMoveDiscardingOne(
            coordinate = playerCoordinate,
            coordinateToDiscard = playerPreviousCoordinate,
            map = map
        ).toMutableList()

        if (candidates.isEmpty()) {
            return RobotPlayerMoveResponse(
                targetCoordinate = playerPreviousCoordinate,
                coordinates = playerPreviousCoordinate,
                previousCoordinates = playerCoordinate,
            )
        }

        var cellToMove: Cell? = null
        var targetCoordinate: Coordinate?

        if (candidates.count() == 1) {
            cellToMove = candidates.single()
            targetCoordinate = Coordinate(cellToMove.x, cellToMove.y)
        } else {
            targetCoordinate = getNearTargetToMove(
                map = map,
                playerCurrentCoordinate = playerCoordinate
            )
            if (targetCoordinate == null) {
                targetCoordinate = getFarTargetToMove(
                    map = map,
                    playerCurrentCoordinate = playerCoordinate
                )
            }
            if (targetCoordinate != null) {
                cellToMove = linearMoveService.getBestCellToMoveComparingDistance(
                    targetCoordinate = targetCoordinate,
                    candidates = candidates
                )
            }
        }

        if (cellToMove != null && targetCoordinate != null) {
            val coordinates = Coordinate(cellToMove.x, cellToMove.y)
            return RobotPlayerMoveResponse(
                targetCoordinate = targetCoordinate,
                coordinates = coordinates,
                previousCoordinates = playerCoordinate,
            )
        }

        return null
    }

    private fun movePlayerToRunFromEnemy(
        map: PMap,
        playerCoordinate: Coordinate,
        moveToDiscard: Coordinate,
    ): RobotPlayerMoveResponse? {
        // get possible cells to move
        // discard the one that was going to
        // move to the first not discarded

        var candidates = linearMoveService.getPossibleCellsToMoveDiscardingOne(
            coordinate = playerCoordinate,
            coordinateToDiscard = moveToDiscard,
            map = map,
        )
        if (candidates.isEmpty()) {
            return null
        }

        candidates = candidates.shuffled()
        val nextMove = Coordinate(candidates.first().x, candidates.first().y)

        return RobotPlayerMoveResponse(
            targetCoordinate = nextMove,
            coordinates = nextMove,
            previousCoordinates = playerCoordinate
        )
    }

    private fun checkIfEnemyIsApproaching(
        map: PMap,
        playerCurrentCoordinate: Coordinate,
        playerNextMove: Coordinate,
        enemies: ImmutableList<GameEnemyState>
    ): Boolean {
        // for only enemies in [Chasing] mode
        // check if enemy and player are in the same row and column
        // check if distance between enemy and player is less or equal to [ROBOT_RUN_FROM_ENEMY_TILES_AMOUNT]
        // check if the close enemy has a clear path to the player.

        // for only enemies in [Chasing] mode
        val enemiesChasing = enemies.filter { it.mode == EnemyMode.Chasing }
        if (enemiesChasing.isEmpty()) {
            return false
        }

        // check if enemy and player are in the same row and column
        val enemiesSameAxe = mutableSetOf<GameEnemyState>()
        for (enemy in enemiesChasing) {
            if (enemy.coordinates.first == playerNextMove.first ||
                enemy.coordinates.second == playerNextMove.second
            ) {
                enemiesSameAxe.add(enemy)
            }
        }
        if (enemiesSameAxe.isEmpty()) {
            return false
        }

        // check if distance between enemy and player is less or equal to [ROBOT_RUN_FROM_ENEMY_TILES_AMOUNT]
        val enemiesClose = mutableSetOf<GameEnemyState>()
        for (enemy in enemiesSameAxe) {
            val distance = linearMoveService.calculateDistance(
                point1 = playerNextMove,
                point2 = enemy.coordinates
            )
            if (distance <= ROBOT_RUN_FROM_ENEMY_TILES_AMOUNT) {
                enemiesClose.add(enemy)
            }
        }
        if (enemiesClose.isEmpty()) {
            return false
        }

        // check if the close enemy has a clear path to the player
        val playerMoveType = linearMoveService.getMoveTypeFromPreviousAndCurrentCoordinates(
            currentCoordinate = playerNextMove,
            previousCoordinate = playerCurrentCoordinate
        )
        var enemyHit: GameEnemyState? = null
        for (enemy in enemiesClose) {
            if (enemyHit != null) {
                break
            }
            var moveCoordinate: Coordinate = playerNextMove
            for (step in 0 until ROBOT_RUN_FROM_ENEMY_TILES_AMOUNT) {
                moveCoordinate = linearMoveService.moveCoordinate(
                    move = playerMoveType,
                    x = moveCoordinate.first,
                    y = moveCoordinate.second
                )
                if (isInGameLimits(moveCoordinate) &&
                    !CELL_TYPE_PLAYER_CAN_MOVE.contains(map[moveCoordinate]!!.type)
                ) {
                    break
                }
                if (isInGameLimits(moveCoordinate) && step == ROBOT_RUN_FROM_ENEMY_TILES_AMOUNT - 1) {
                    enemyHit = enemy
                }
            }
        }

        return enemyHit != null
    }

    private fun isInGameLimits(
        candidate: Coordinate
    ): Boolean {
        return candidate.first in 0..<X_TILE_COUNT &&
                candidate.second in 0..<Y_TILE_COUNT
    }

    private fun isLetterUndone(
        map: PMap,
        candidate: Coordinate,
    ): Boolean {
        return CELL_TYPE_PLAYER_CAN_MOVE.contains(map[candidate]!!.type) &&
                !map[candidate]!!.letterDone
    }

    private fun getNearTargetToMove(
        map: PMap,
        playerCurrentCoordinate: Coordinate,
    ): Coordinate? {
        // from the player position
        // checks for each move clock wise [ROBOT_NEAR_TILES_TO_CHECK_AMOUNT] tiles ahead
        // if a letter is undone

        var output: Coordinate? = null
        val discardedMovTypes = mutableSetOf<MoveType>()
        val coordinatesPerMoveType = mutableMapOf<MoveType, Coordinate>()
        val moveTypes = MoveType.entries.shuffled()

        for (steps in 0 until ROBOT_NEAR_TILES_TO_CHECK_AMOUNT) {
            if (output != null) {
                break
            }
            for (moveType in moveTypes) {
                if (discardedMovTypes.contains(moveType)) {
                    continue
                }

                val candidate = linearMoveService.moveCoordinate(
                    move = moveType,
                    x = coordinatesPerMoveType[moveType]?.first ?: playerCurrentCoordinate.first,
                    y = coordinatesPerMoveType[moveType]?.second ?: playerCurrentCoordinate.second
                )

                if (!isInGameLimits(candidate)) {
                    discardedMovTypes.add(moveType)
                    continue
                }
                if (isLetterUndone(map = map, candidate = candidate)) {
                    output = candidate
                    break
                } else {
                    coordinatesPerMoveType[moveType] = candidate
                }
            }
        }

        return output
    }

    private fun getFarTargetToMove(
        map: PMap,
        playerCurrentCoordinate: Coordinate,
    ): Coordinate? {
        // scans the full row [X axes] where the player is, Y fixed,
        // if there is not undone letter
        // scan Y column, starting from the closer to border
        var output: Coordinate? = null
        var candidateFullRow = playerCurrentCoordinate

        // scan X row where the player is, Y fixed
        for (x in 0 until X_TILE_COUNT) {
            if (x != candidateFullRow.first) {
                candidateFullRow = linearMoveService.moveCoordinate(
                    move = MoveType.Right,
                    x = x,
                    y = candidateFullRow.second
                )
                if (isInGameLimits(candidateFullRow)
                    && isLetterUndone(map = map, candidate = candidateFullRow)
                ) {
                    output = candidateFullRow
                    break
                }
            }
        }
        if (output != null) {
            return output
        }

        // scan Y column, starting from the closer to border
        val candidateFullScan = playerCurrentCoordinate
        val yDistanceToBottom = Y_TILE_COUNT - 1 - candidateFullScan.second
        val yDistanceToTop = candidateFullScan.second
        val closeToBottom = yDistanceToBottom <= yDistanceToTop

        if (closeToBottom) {
            output = farTargetToBottomLoop(map, candidateFullScan)
            if (output == null) {
                output = farTargetToTopLoop(map, candidateFullScan)
            }
        } else {
            output = farTargetToTopLoop(map, candidateFullScan)
            if (output == null) {
                output = farTargetToBottomLoop(map, candidateFullScan)
            }
        }

        return output
    }

    private fun farTargetToBottomLoop(
        map: PMap,
        candidate: Coordinate
    ): Coordinate? {
        var output: Coordinate? = null
        for (y in candidate.second + 1 until Y_TILE_COUNT) {
            output = farTargetFullRowByColumn(y = y, map = map)
            if (output != null) {
                break
            }
        }
        return output
    }

    private fun farTargetToTopLoop(
        map: PMap,
        candidate: Coordinate
    ): Coordinate? {
        var output: Coordinate? = null
        for (y in candidate.second - 1 downTo 0) {
            output = farTargetFullRowByColumn(y = y, map = map)
            if (output != null) {
                break
            }
        }
        return output
    }

    private fun farTargetFullRowByColumn(
        y: Int,
        map: PMap
    ): Coordinate? {
        var output: Coordinate? = null
        for (x in 0 until X_TILE_COUNT) {
            val candidate = linearMoveService.moveCoordinate(
                move = MoveType.Right,
                x = x,
                y = y
            )
            if (isInGameLimits(candidate) && isLetterUndone(map = map, candidate = candidate)) {
                output = candidate
                break
            }
        }
        return output
    }

}

data class RobotPlayerMoveResponse(
    val coordinates: Coordinate,
    val previousCoordinates: Coordinate,
    val targetCoordinate: Coordinate,
)
