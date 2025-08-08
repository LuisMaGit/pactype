package com.luisma.pactype.views.game.services.enemies_services

import com.luisma.pactype.views.game.data.CELL_TYPE_PLAYER_CAN_MOVE
import com.luisma.pactype.views.game.data.Cell
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.data.EnemyMode
import com.luisma.pactype.views.game.data.GameContractsByMapType
import com.luisma.pactype.views.game.data.PMap
import com.luisma.pactype.views.game.services.EnemiesData
import com.luisma.pactype.views.game.services.LinearMoveService
import com.luisma.pactype.views.game.states.GameEnemyState

class MoveEnemiesService(
    private val linearMoveService: LinearMoveService,
    private val enemyTargetCoordinateService: EnemyTargetCoordinateService
) {

    fun startEnemiesPosition(
        contractsFromMap: GameContractsByMapType,
        enemies: List<EnemiesData>,
    ): List<GameEnemyState> {
        val output = mutableListOf<GameEnemyState>()
        val enemyTypes = enemies.map { it.enemyType }
        enemies.forEachIndexed { idx, enemy ->
            val coordinates = Coordinate(
                contractsFromMap.firstEnemyStartCoordinate.first + enemyTypes.indexOf(enemy.enemyType),
                contractsFromMap.firstEnemyStartCoordinate.second
            )
            output.add(
                GameEnemyState(
                    enemyType = enemy.enemyType,
                    mode = EnemyMode.WaitingInHome,
                    startTrigger = idx == 0,
                    moveDelayMS = enemy.enemyVelocityMs,
                    startMoveDelaySec = enemy.enemyDelaySec,
                    targetCoordinate = coordinates,
                    coordinates = coordinates,
                    prevCoordinate = Coordinate(-1, -1),
                )
            )
        }
        return output
    }

    // check if enemy is in home
    // if it's in home -> moveEnemyInHome
    // not in home -> moveEnemyInMaze
    fun moveEnemy(
        contractsFromMap: GameContractsByMapType,
        map: PMap,
        enemy: GameEnemyState,
        playerCoordinate: Coordinate,
        playerPreviousCoordinate: Coordinate,
    ): GameEnemyState {
        if (enemy.mode == EnemyMode.WaitingInHome ||
            enemy.mode == EnemyMode.MovingInHome
        ) {
            return moveEnemyInHome(
                contractsFromMap = contractsFromMap,
                map = map,
                enemy = enemy
            )
        }

        return moveEnemyInMaze(
            map = map,
            enemy = enemy,
            playerCoordinate = playerCoordinate,
            playerPreviousCoordinate = playerPreviousCoordinate
        )
    }

    // moves the enemy inside the home,
    // changes the state to [EnemyMode.Chasing] when is out of home
    private fun moveEnemyInHome(
        contractsFromMap: GameContractsByMapType,
        map: PMap,
        enemy: GameEnemyState,
    ): GameEnemyState {

        // change the state to moving in home
        // check if it has arrived, not arrived, or passed the break point
        // passed -> move Y and check if it's out of home
        // arrived -> move Y
        // not arrived -> check if it needs to move left or right and move X

        var output = if (enemy.mode == EnemyMode.WaitingInHome) {
            enemy.copy(mode = EnemyMode.MovingInHome)
        } else {
            enemy
        }
        val breakPoint = contractsFromMap.enemyHomeBreakCoordinate
        val xBreakPoint = breakPoint.first
        val yBreakPoint = breakPoint.second
        val xEnemy = output.coordinates.first
        val yEnemy = output.coordinates.second

        // passed break point or arrived break point: move y
        if (yEnemy < yBreakPoint || breakPoint == enemy.coordinates) {
            val newCoordinate = Coordinate(xEnemy, yEnemy - 1)
            val newCellType = map[newCoordinate]!!.type
            output = output.copy(
                coordinates = newCoordinate,
                mode = if (CELL_TYPE_PLAYER_CAN_MOVE.contains(newCellType)) {
                    EnemyMode.Chasing
                } else {
                    output.mode
                },
                prevCoordinate = output.coordinates,
            )
        }
        // not arrived break point: move x
        else {
            val newXEnemy = if (xEnemy < xBreakPoint) {
                xEnemy + 1
            } else {
                xEnemy - 1
            }
            output = output.copy(
                coordinates = Coordinate(newXEnemy, yEnemy),
                prevCoordinate = output.coordinates,
            )
        }
        return output
    }

    // moves the enemy in the maze, out of home.
    private fun moveEnemyInMaze(
        map: PMap,
        enemy: GameEnemyState,
        playerCoordinate: Coordinate,
        playerPreviousCoordinate: Coordinate,
    ): GameEnemyState {
        // get possible moves (with previous move discarded)
        // if it is only one, move there
        // more than one, make the math to get the best cell to move

        val candidates = linearMoveService.getPossibleCellsToMoveDiscardingOne(
            coordinate = enemy.coordinates,
            coordinateToDiscard = enemy.prevCoordinate,
            map = map
        )

        val cellToMove: Cell
        val targetCoordinate: Coordinate

        if (candidates.count() == 1) {
            cellToMove = candidates.single()
            targetCoordinate = Coordinate(cellToMove.x, cellToMove.y)
        } else {
            targetCoordinate = enemyTargetCoordinateService.getTargetCoordinate(
                map = map,
                enemy = enemy,
                playerCoordinate = playerCoordinate,
                playerPreviousCoordinate = playerPreviousCoordinate
            )
            cellToMove = linearMoveService.getBestCellToMoveComparingDistance(
                candidates = candidates,
                targetCoordinate = targetCoordinate
            )
        }

        return linearMoveService.moveEnemyToCell(
            cell = cellToMove,
            targetCoordinate = targetCoordinate,
            enemy = enemy,
        )
    }

}