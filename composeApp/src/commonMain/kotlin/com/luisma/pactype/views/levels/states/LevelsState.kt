package com.luisma.pactype.views.levels.states

import androidx.compose.runtime.Immutable
import com.luisma.pactype.ui.theme.LevelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


@Immutable
data class LevelsState(
    val total: Int,
    val currentOnScreen: Int,
    val lastPlayed: Int,
    val openedAppForFirstTime: Boolean,
    val levels: ImmutableList<LevelState>
) {
    companion object {
        fun initial(): LevelsState {
            return LevelsState(
                total = 0,
                currentOnScreen = -1,
                lastPlayed = -1,
                openedAppForFirstTime = false,
                levels = persistentListOf()
            )
        }
    }
}

@Immutable
data class LevelState(
    val levelId: Int,
    val levelName: String,
    val levelTheme: LevelTheme,
    val playModeBestTimeCS: Int,
    val playModeProgress: Int,
    val practiceModeBestTimeCS: Int,
) {
    companion object {
        fun initial(): LevelState {
            return LevelState(
                levelId = -1,
                levelName = "",
                playModeProgress = 0,
                playModeBestTimeCS = 0,
                practiceModeBestTimeCS = 0,
                levelTheme = LevelTheme.initial()
            )
        }
    }

}