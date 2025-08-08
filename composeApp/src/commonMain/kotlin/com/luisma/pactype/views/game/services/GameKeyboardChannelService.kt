package com.luisma.pactype.views.game.services

import com.luisma.pactype.views.game.data.NON_LETTERS_SUPPORTED
import com.luisma.pactype.views.game.data.SPACE_CHAR
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object GameKeyboardChannelService {
    private val keyChannel = Channel<Char?>()
    val keyChannelFlow = keyChannel.receiveAsFlow()

    suspend fun emitKey(char: Char) {
        keyChannel.send(char)
    }

    fun validateChar(char: Char, isShiftPressed: Boolean): Char? {
        return if (char.isLetter()) {
            if (isShiftPressed) char.uppercaseChar() else char.lowercaseChar()
        } else if (NON_LETTERS_SUPPORTED.contains(char)) {
            if (char == ' ') SPACE_CHAR else char
        } else {
            null
        }
    }
}