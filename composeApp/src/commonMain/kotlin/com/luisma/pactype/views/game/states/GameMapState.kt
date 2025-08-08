package com.luisma.pactype.views.game.states

import androidx.compose.runtime.Immutable
import com.luisma.pactype.views.game.data.MapType
import com.luisma.pactype.views.game.data.PMap
import kotlinx.collections.immutable.persistentMapOf

@Immutable
data class GameMapState(
    val levelId: Int,
    val map: PMap,
    val mapType: MapType,
    val totalChars: Int,
    val showDebugGrid: Boolean,
    val levelName: String
) {
    companion object {
        fun initial(): GameMapState {
            return GameMapState(
                levelId = -1,
                map = persistentMapOf(),
                mapType = -1,
                totalChars = -1,
                showDebugGrid = false,
                levelName = ""
            )
        }
    }
}

