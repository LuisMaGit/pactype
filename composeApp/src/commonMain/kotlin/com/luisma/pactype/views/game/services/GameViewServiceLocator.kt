package com.luisma.pactype.views.game.services

import com.luisma.pactype.services.levelsDBService
import com.luisma.pactype.views.game.services.enemies_services.EnemyTargetCoordinateService
import com.luisma.pactype.views.game.services.enemies_services.MoveEnemiesService

fun gameKeyboardChannelService(): GameKeyboardChannelService {
    return GameKeyboardChannelService
}

fun linearMoveService(): LinearMoveService {
    return LinearMoveService()
}

fun enemyTargetCoordinateService(): EnemyTargetCoordinateService {
    return EnemyTargetCoordinateService(
        linearMoveService = linearMoveService()
    )
}

fun moveEnemiesService(): MoveEnemiesService {
    return MoveEnemiesService(
        linearMoveService = linearMoveService(),
        enemyTargetCoordinateService = enemyTargetCoordinateService()
    )
}

fun robotPlayerService(): RobotPlayerService {
    return RobotPlayerService(
        linearMoveService = linearMoveService(),
    )
}

fun gameLevelService(): GameLevelService {
    return GameLevelService(
        levelsDBService = levelsDBService()
    )
}
