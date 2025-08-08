package com.luisma.pactype.services.db_services

import com.luisma.pactype.services.db_services.db_models.UserProgressDBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UserProgressDBService {

    private val queries = DBInstance.db.user_progress_queryQueries

    suspend fun getProgress(levelId: Int): UserProgressDBModel {
        return withContext(Dispatchers.IO) {
            val db = queries.selectLevelProgress(levelId.toLong()).executeAsOneOrNull()
            UserProgressDBModel(
                levelId = db?.level_id?.toInt() ?: -1,
                practiceModeBestTimeCS = db?.practice_mode_best_time_cs?.toInt() ?: 0,
                playModeProgress = db?.play_mode_progress?.toInt() ?: 0,
                playModeBestTimeCS = db?.play_mode_best_time_cs?.toInt() ?: 0,
            )
        }
    }

    suspend fun insertProgress(dbProgress: UserProgressDBModel) {
        return withContext(Dispatchers.IO) {
            queries.insertProgress(
                level_id = dbProgress.levelId.toLong(),
                play_mode_progress = dbProgress.playModeProgress.toLong(),
                practice_mode_best_time_cs = dbProgress.practiceModeBestTimeCS.toLong(),
                play_mode_best_time_cs = dbProgress.playModeBestTimeCS.toLong()
            )
        }
    }

    suspend fun updateProgress(dbProgress: UserProgressDBModel) {
        return withContext(Dispatchers.IO) {
            queries.updateProgress(
                level_id = dbProgress.levelId.toLong(),
                play_mode_progress = dbProgress.playModeProgress.toLong(),
                play_mode_best_time_cs = dbProgress.playModeBestTimeCS.toLong(),
                practice_mode_best_time_cs = dbProgress.practiceModeBestTimeCS.toLong()
            )
        }
    }

    suspend fun selectAllProgress(): List<UserProgressDBModel> {
        return withContext(Dispatchers.IO) {
            queries.selectAllProgress().executeAsList().map {
                UserProgressDBModel(
                    levelId = it.level_id.toInt(),
                    practiceModeBestTimeCS = it.practice_mode_best_time_cs?.toInt() ?: 0,
                    playModeProgress = it.play_mode_progress?.toInt() ?: 0,
                    playModeBestTimeCS = it.play_mode_best_time_cs?.toInt() ?: 0,
                )
            }
        }
    }
}