package com.luisma.pactype.views.game

import com.luisma.pactype.views.game.data.PKey

sealed class GameEvents {
    data class OnCreate(val levelId: Int, val practiceMode: Boolean) : GameEvents()
    data class OnPause(val setPause: Boolean) : GameEvents()
    data object OnReset : GameEvents()
    data class KeyboardTap(val key: PKey) : GameEvents()
    data object GoBack : GameEvents()
}