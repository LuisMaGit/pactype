package com.luisma.pactype.views.levels.service

import com.luisma.pactype.views.levels.data.LevelsNavigationKeys
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object LevelsKeyboardChannelService {
    private val keyChannel = Channel<LevelsNavigationKeys>()
    val keyChannelFlow = keyChannel.receiveAsFlow()

    fun validateChar(char: Char) : Int {
       return LevelsNavigationKeys.entries.map { it.char }.indexOf(char)
    }

    suspend fun sendChar(idxChar: Int) {
        keyChannel.send(LevelsNavigationKeys.entries[idxChar])
    }
}