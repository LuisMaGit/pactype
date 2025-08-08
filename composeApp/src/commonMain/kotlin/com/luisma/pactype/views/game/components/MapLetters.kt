package com.luisma.pactype.views.game.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.luisma.pactype.views.game.data.X_TILE_COUNT
import com.luisma.pactype.views.game.data.Y_TILE_COUNT
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.utils.fontSizeNonScaledSp
import com.luisma.pactype.views.game.data.CELL_TYPE_PLAYER_CAN_MOVE
import com.luisma.pactype.views.game.data.Coordinate
import com.luisma.pactype.views.game.data.PMap

@Composable
fun MapLetters(
    modifier: Modifier = Modifier,
    map: PMap,
    tileSize: Dp,
    fontSize: Int,
    colorOn: Color,
    colorOff: Color,
) {
    for (y in 0..<Y_TILE_COUNT) {
        for (x in 0..<X_TILE_COUNT) {
            val cell = map[Coordinate(x, y)]!!
            if (CELL_TYPE_PLAYER_CAN_MOVE.contains(cell.type))
                Box(
                    modifier = Modifier
                        .size(tileSize)
                        .offset(x = tileSize * x, y = tileSize * y),
                    contentAlignment = Alignment.Center,
                ) {
                    PText(
                        modifier = Modifier,
                        text = cell.char.toString(),
                        fontSize = fontSize.fontSizeNonScaledSp,
                        color = if (cell.enableLetter) colorOff else colorOn,
                        textAlign = TextAlign.Center,
                    )
                }
        }
    }

}