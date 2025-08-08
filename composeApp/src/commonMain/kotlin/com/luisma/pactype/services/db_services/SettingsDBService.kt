package com.luisma.pactype.services.db_services

import com.luisma.pactype.services.db_services.db_models.SettingsDBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SettingsDBService {
    private val queries = DBInstance.db.user_settings_queryQueries


    suspend fun getSettings(): SettingsDBModel {
        return withContext(Dispatchers.IO) {
            val db = queries.selectSettings().executeAsOneOrNull()
            val firstTime = db?.first_time?.toInt()
            SettingsDBModel(
                isFirstTime = firstTime == null || firstTime == 1,
                showKeyboard = db?.show_keyboard?.toInt() == 1,
                lastPlayedLevelId = db?.last_played_level_id?.toInt(),
            )
        }
    }

    suspend fun insertSettings(settings: SettingsDBModel) {
        withContext(Dispatchers.IO) {
            queries.insertSettings(
                show_keyboard = if (settings.showKeyboard) 1 else 0,
                first_time = if (settings.isFirstTime) 1 else 0,
            )
        }
    }

    suspend fun updateLastPlayedLevel(levelId: Int) {
        withContext(Dispatchers.IO) {
            queries.updateLastPlayedId(last_played_level_id = levelId.toLong())
        }
    }

    suspend fun updateShowKeyboard(show: Boolean) {
        withContext(Dispatchers.IO) {
            queries.updateShowKeyboard(show_keyboard = if (show) 1 else 0)
        }
    }

}