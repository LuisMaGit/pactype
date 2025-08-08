package com.luisma.pactype.views.levels

sealed class LevelsEvents {
    data object OnCreate : LevelsEvents()
    data object ToggleSettingsDialog : LevelsEvents()
    data object ToggleTutorialDialog : LevelsEvents()
    data class SetLevelOnScreen(val id: Int) : LevelsEvents()
    data class OnCreateLevel(val goToPageCallback: suspend (page: Int) -> Unit) : LevelsEvents()
    data class GoToPage(val page: Int) : LevelsEvents()
    data object GoToLastPlayed : LevelsEvents()
    data class GoToGame(val levelId: Int, val practiceMode: Boolean) : LevelsEvents()
    data object OnDispose : LevelsEvents()

    fun isDialogEvent(): Boolean {
        return this == ToggleSettingsDialog || this == ToggleTutorialDialog
    }

}