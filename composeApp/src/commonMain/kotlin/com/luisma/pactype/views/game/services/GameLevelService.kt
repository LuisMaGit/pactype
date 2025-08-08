package com.luisma.pactype.views.game.services

import com.luisma.pactype.services.db_services.LevelsDBService
import com.luisma.pactype.ui.theme.LevelTheme
import com.luisma.pactype.views.game.data.Cell
import com.luisma.pactype.views.game.data.CellType
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.data.DISABLED_CHAR
import com.luisma.pactype.views.game.data.ENEMY_HOME_CHAR
import com.luisma.pactype.views.game.data.EnemyType
import com.luisma.pactype.views.game.data.MapType
import com.luisma.pactype.views.game.data.PMap
import com.luisma.pactype.views.game.data.SPACE_CHAR
import kotlinx.collections.immutable.toImmutableMap

class GameLevelService(
    private val levelsDBService: LevelsDBService
) {

    suspend fun getGameData(levelId: Int): GameData {
        val levelDB = levelsDBService.getLevel(levelId)
        val map = mutableMapOf<Coordinate, Cell>()
        var x = 0
        var y = 0
        var lettersCount = 0
        var spaceCount = 0

        for (char in levelDB.charMap) {
            if (char == '\n') {
                y++
                x = 0
            } else {
                val cellType = when (char) {
                    DISABLED_CHAR -> {
                        CellType.Disabled
                    }

                    ENEMY_HOME_CHAR -> {
                        CellType.EnemyHome
                    }

                    SPACE_CHAR -> {
                        spaceCount++
                        CellType.Space
                    }

                    else -> {
                        lettersCount++
                        CellType.Letter
                    }
                }
                map[Coordinate(x, y)] = Cell(
                    x = x,
                    y = y,
                    type = cellType,
                    char = char,
                    letterDone = false,
                    enableLetter = false
                )
                x++
            }
        }


        val charCount = lettersCount + spaceCount
        return GameData(
            levelId = levelDB.id,
            name = levelDB.name,
            map = map.toImmutableMap(),
            mapType = levelDB.mapType,
            charCount = charCount,
            levelTheme = LevelTheme(
                main = levelDB.levelDBColors.main,
                background = levelDB.levelDBColors.background,
                textOn = levelDB.levelDBColors.textOn,
                textOff = levelDB.levelDBColors.textOff,
                key = levelDB.levelDBColors.key,
            )
        )
    }

    suspend fun getEnemiesDataInLevel(levelId: Int): List<EnemiesData> {
        return levelsDBService.getEnemiesInLevel(levelId)
            .map { enemy ->
                EnemiesData(
                    enemyType = EnemyType.getTypeByDBOrdinal(enemy.enemyId),
                    enemyVelocityMs = enemy.velocityMs,
                    enemyDelaySec = enemy.delaySec
                )
            }
    }

}

data class GameData(
    val levelId: Int,
    val name: String,
    val map: PMap,
    val mapType: MapType,
    val charCount: Int,
    val levelTheme: LevelTheme
)

data class EnemiesData(
    val enemyType: EnemyType,
    val enemyVelocityMs: Int,
    val enemyDelaySec: Int
)