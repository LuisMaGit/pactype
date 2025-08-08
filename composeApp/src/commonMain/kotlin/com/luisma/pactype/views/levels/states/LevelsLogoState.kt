package com.luisma.pactype.views.levels.states

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class LevelsLogoState(
    val animationDone: Boolean,
    val caretOffset: Int,
    val caretOffsetPrev: Int,
    val chars: ImmutableList<LogoWord>,
    val charToggleDelayMS: Int,
    val charWidth: Int,
) {
    companion object {
        fun initial(): LevelsLogoState {
            return LevelsLogoState(
                animationDone = false,
                caretOffset = 0,
                caretOffsetPrev = 0,
                charToggleDelayMS = 40,
                charWidth = 20,
                chars = persistentListOf(
                    LogoWord(' ', done = false),
                    LogoWord('p', done = false),
                    LogoWord('a', done = false),
                    LogoWord('c', done = false),
                    LogoWord('t', done = false),
                    LogoWord('y', done = false),
                    LogoWord('p', done = false),
                    LogoWord('e', done = false),
                )
            )
        }
    }
}

data class LogoWord(
    val char: Char,
    val done: Boolean,
)