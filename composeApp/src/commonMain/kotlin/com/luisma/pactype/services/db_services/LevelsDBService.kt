package com.luisma.pactype.services.db_services

import com.luisma.database.Level_colors
import com.luisma.database.Level_enemies
import com.luisma.database.SelectAllLevelsBasics
import com.luisma.database.SelectLevel
import com.luisma.pactype.services.db_services.db_models.LevelDBColors
import com.luisma.pactype.services.db_services.db_models.LevelDBModel
import com.luisma.pactype.services.db_services.db_models.LevelEnemiesDBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class LevelsDBService {
    private val queries = DBInstance.db.level_queryQueries

    private fun mapLevel(levels: SelectLevel): LevelDBModel {
        return LevelDBModel(
            id = levels.level_id.toInt(),
            name = levels.name,
            charMap = levels.char_map,
            mapType = levels.char_map_type.toInt(),
            difficulty = levels.difficulty.toInt(),
            levelDBColors = LevelDBColors(
                main = levels.color_main,
                background = levels.color_background,
                textOn = levels.color_text_on,
                textOff = levels.color_text_off,
                key = levels.color_key,
            )
        )
    }

    private fun mapLevelsBasic(
        levels: List<SelectAllLevelsBasics>,
        colors: List<Level_colors>
    ): List<LevelDBModel> {
        return levels.map { level ->
            val color = colors.first { color -> color.level_id == level.level_id }
            LevelDBModel(
                id = level.level_id.toInt(),
                name = level.name,
                charMap = "",
                mapType = -1,
                difficulty = level.difficulty.toInt(),
                levelDBColors = LevelDBColors(
                    main = color.color_main,
                    background = color.color_background,
                    textOn = color.color_text_on,
                    textOff = color.color_text_off,
                    key = color.color_key,
                )
            )
        }
    }

    private fun mapLevelEnemies(levels: List<Level_enemies>): List<LevelEnemiesDBModel> {
        return levels.map { db ->
            LevelEnemiesDBModel(
                levelId = db.level_id.toInt(),
                enemyId = db.enemy_id.toInt(),
                delaySec = db.delay_sec.toInt(),
                velocityMs = db.velocity_ms.toInt()
            )
        }
    }

    suspend fun getLevel(levelID: Int): LevelDBModel {
        return withContext(Dispatchers.IO) {
            mapLevel(queries.selectLevel(levelID.toLong()).executeAsOne())
        }
    }

    suspend fun getAllLevelsBasics(): List<LevelDBModel> {
        return withContext(Dispatchers.IO) {
            val levels = async { queries.selectAllLevelsBasics().executeAsList() }.await()
            val colors = async { queries.selectAllLevelColors().executeAsList() }.await()
            mapLevelsBasic(levels, colors)
        }
    }

    suspend fun getEnemiesInLevel(levelID: Int): List<LevelEnemiesDBModel> {
        return withContext(Dispatchers.IO) {
            mapLevelEnemies(queries.selectLevelEnemies(levelID.toLong()).executeAsList())
        }
    }

}