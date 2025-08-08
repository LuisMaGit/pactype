package com.luisma.pactype.views.levels.states

import androidx.compose.runtime.Immutable


@Immutable
data class LevelsStatusState(
    val status: LevelsStatus,
    val showSettingsDialog: Boolean,
    val showTutorialDialog: Boolean,
    val levelInit: Boolean,
    val showLastPlayedBanner: Boolean,
) {
    companion object {
        fun initial(): LevelsStatusState {
            return LevelsStatusState(
                status = LevelsStatus.Loading,
                showSettingsDialog = false,
                showTutorialDialog = false,
                levelInit = false,
                showLastPlayedBanner = false
            )
        }
    }

    val dialogOpen get() = showTutorialDialog || showSettingsDialog
}


enum class LevelsStatus {
    Loading,
    Success
}