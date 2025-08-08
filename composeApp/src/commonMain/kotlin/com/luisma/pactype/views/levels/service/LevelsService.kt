package com.luisma.pactype.views.levels.service

import com.luisma.pactype.platform.SupportedPlatforms
import com.luisma.pactype.services.PlatformService
import com.luisma.pactype.services.db_services.LevelsDBService
import com.luisma.pactype.services.db_services.SettingsDBService
import com.luisma.pactype.services.db_services.UserProgressDBService
import com.luisma.pactype.services.db_services.db_models.SettingsDBModel
import com.luisma.pactype.ui.theme.LevelTheme
import com.luisma.pactype.views.levels.states.LevelState
import com.luisma.pactype.views.levels.states.LevelsState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class LevelsService(
    private val userProgressDBService: UserProgressDBService,
    private val levelsDBService: LevelsDBService,
    private val settingsDBService: SettingsDBService,
    private val platformService: PlatformService,
) {

    suspend fun getAllLevels(): LevelsState {
        return coroutineScope {
            val progressDeferred = async { userProgressDBService.selectAllProgress() }
            val levelsDeferred = async { levelsDBService.getAllLevelsBasics() }
            val settingsDeferred = async { settingsDBService.getSettings() }

            val progress = progressDeferred.await()
            val levels = levelsDeferred.await()
            val settings = settingsDeferred.await()

            val isFirstTime = settings.isFirstTime
            if (isFirstTime) {
                val platform = platformService.platform()
                settingsDBService.insertSettings(
                    SettingsDBModel(
                        isFirstTime = false,
                        showKeyboard = platform == SupportedPlatforms.IOS ||
                                platform == SupportedPlatforms.ANDROID
                    )
                )
            }

            val levelsOutput = mutableListOf<LevelState>()
            for (level in levels) {
                val progressExists = progress.firstOrNull { it.levelId == level.id }
                levelsOutput.add(
                    LevelState(
                        levelId = level.id,
                        levelName = level.name,
                        playModeProgress = progressExists?.playModeProgress ?: 0,
                        practiceModeBestTimeCS = progressExists?.practiceModeBestTimeCS ?: 0,
                        playModeBestTimeCS = progressExists?.playModeBestTimeCS ?: 0,
                        levelTheme = LevelTheme(
                            main = level.levelDBColors.main,
                            background = level.levelDBColors.background,
                            textOn = level.levelDBColors.textOn,
                            textOff = level.levelDBColors.textOff,
                            key = level.levelDBColors.key,
                        )
                    )
                )
            }
            val lastPlayedId = settings.lastPlayedLevelId ?: 1
            return@coroutineScope LevelsState(
                currentOnScreen = lastPlayedId,
                lastPlayed = lastPlayedId,
                levels = levelsOutput.toImmutableList(),
                total = levelsOutput.size,
                openedAppForFirstTime = isFirstTime,
            )
        }
    }

    suspend fun getShowKeyboard(): Boolean {
        val db = settingsDBService.getSettings()
        return db.showKeyboard
    }

    suspend fun setShowKeyboard(show: Boolean) {
        settingsDBService.updateShowKeyboard(show)
    }

}


