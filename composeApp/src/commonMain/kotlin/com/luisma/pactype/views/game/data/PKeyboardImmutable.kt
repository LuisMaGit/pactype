package com.luisma.pactype.views.game.data


import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class PKeyboard(
    val row1: KeyboardState,
    val row2: KeyboardState,
    val row3: KeyboardState,
    val row4: KeyboardState,
) {
    companion object {
        fun fullKeyboard(): PKeyboard {
            return PKeyboard(
                row1 = persistentListOf(
                    PKey.letter(capital = 'Q', lower = 'q'),
                    PKey.letter(capital = 'W', lower = 'w'),
                    PKey.letter(capital = 'E', lower = 'e'),
                    PKey.letter(capital = 'R', lower = 'r'),
                    PKey.letter(capital = 'T', lower = 't'),
                    PKey.letter(capital = 'Y', lower = 'y'),
                    PKey.letter(capital = 'U', lower = 'u'),
                    PKey.letter(capital = 'I', lower = 'i'),
                    PKey.letter(capital = 'O', lower = 'o'),
                    PKey.letter(capital = 'P', lower = 'p'),
                ),
                row2 = persistentListOf(
                    PKey.letter(capital = 'A', lower = 'a'),
                    PKey.letter(capital = 'S', lower = 's'),
                    PKey.letter(capital = 'D', lower = 'd'),
                    PKey.letter(capital = 'F', lower = 'f'),
                    PKey.letter(capital = 'G', lower = 'g'),
                    PKey.letter(capital = 'H', lower = 'h'),
                    PKey.letter(capital = 'J', lower = 'j'),
                    PKey.letter(capital = 'K', lower = 'k'),
                    PKey.letter(capital = 'L', lower = 'l'),
                ),
                row3 = persistentListOf(
                    PKey.shift(),
                    PKey.letter(capital = 'Z', lower = 'z'),
                    PKey.letter(capital = 'X', lower = 'x'),
                    PKey.letter(capital = 'C', lower = 'c'),
                    PKey.letter(capital = 'V', lower = 'v'),
                    PKey.letter(capital = 'B', lower = 'b'),
                    PKey.letter(capital = 'N', lower = 'n'),
                    PKey.letter(capital = 'M', lower = 'm'),
                    PKey.shift(),
                ),
                row4 = persistentListOf(
                    PKey.comma(),
                    PKey.space(),
                    PKey.dot(),
                )
            )
        }
    }

}

typealias KeyboardState = ImmutableList<PKey>


@Immutable
data class PKey(
    val lower: Char,
    val capital: Char,
    val type: PKeyType,
) {
    companion object {
        fun letter(capital: Char, lower: Char): PKey {
            return PKey(
                lower = lower,
                capital = capital,
                type = PKeyType.Letter
            )
        }

        fun shift(): PKey {
            return PKey(
                lower = '⇧',
                capital = '⇪',
                type = PKeyType.Shift
            )
        }

        fun space(): PKey {
            return PKey(
                lower = '␣',
                capital = '␣',
                type = PKeyType.Space
            )
        }

        fun comma(): PKey {
            return PKey(
                lower = ',',
                capital = ',',
                type = PKeyType.Comma
            )
        }

        fun dot(): PKey {
            return PKey(
                lower = '.',
                capital = '.',
                type = PKeyType.Dot
            )
        }
    }
}

enum class PKeyType {
    Letter,
    Shift,
    Space,
    Comma,
    Dot
}