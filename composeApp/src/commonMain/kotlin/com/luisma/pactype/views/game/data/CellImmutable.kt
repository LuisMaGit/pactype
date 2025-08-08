package com.luisma.pactype.views.game.data

import androidx.compose.runtime.Immutable

@Immutable
data class Cell(
    val x: X,
    val y: Y,
    val type: CellType,
    val char: Char,
    val letterDone: Boolean,
    val enableLetter: Boolean,
)

enum class CellType {
    Disabled,
    Letter,
    EnemyHome,
    Space
}