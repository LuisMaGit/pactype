fun checkAndPrintWrongCharsInGameMap(map: PMap) {
    val togetherChars = mutableListOf<TogetherChars>()
    val equalCharsNextMove = mutableListOf<TogetherChars>()
    for (x in 0..<xCount) {
        for (y in 0..<yCount) {
            //1- check move is in limit
            //2- check move can be done
            //3- check current letter match the move letter
            val cell = map[Coordinate(x, y)]!!
            if (cell.type == CellType.Letter || cell.type == CellType.Space) {
                val moveThatCanBeDoneForChar = mutableListOf<MoveThatCanBeDoneForChar>()
                for (move in MoveType.entries) {
                    if (checkMoveIsInLimit(x = x, y = y, move = move)) {
                        val nextMove = checkMoveCanBeDone(x = x, y = y, move = move, map = map)
                        if (nextMove.canBeDone) {
                            // together chars
                            if (cell.char == nextMove.moveCell.char) {
                                togetherChars.add(
                                    TogetherChars(
                                        char = cell.char,
                                        charCoordinate = Coordinate(x, y),
                                        nextMoveCharCoordinate = Coordinate(nextMove.moveCell.x, nextMove.moveCell.y),
                                        nextMoveChar = cell.char
                                    )
                                )
                            }
                            // save move that can be done
                            moveThatCanBeDoneForChar.add(
                                MoveThatCanBeDoneForChar(
                                    move = move,
                                    moveCell = nextMove.moveCell
                                )
                            )
                        }
                    }
                }

                // check if move that can be done has matching chars
                if (moveThatCanBeDoneForChar.isNotEmpty()) {
                    val seen = mutableListOf<Char>()
                    moveThatCanBeDoneForChar.forEach {
                        if (seen.contains(it.moveCell.char)) {
                            equalCharsNextMove.add(
                                TogetherChars(
                                    char = cell.char,
                                    charCoordinate = Coordinate(x, y),
                                    nextMoveChar = it.moveCell.char,
                                    nextMoveCharCoordinate = Coordinate(it.moveCell.x, it.moveCell.y),
                                )
                            )
                        }
                        seen.add(it.moveCell.char)
                    }
                    moveThatCanBeDoneForChar.clear()
                }

            }
        }
    }

    togetherChars.forEach {
        println("Together chars: ${it.char} ${it.charCoordinate} with next move ${it.nextMoveCharCoordinate}")
    }

    equalCharsNextMove.forEach {
        println("Equals chars in move possibilities: ${it.char} ${it.charCoordinate} with move to ${it.nextMoveChar} ${it.nextMoveCharCoordinate}")
    }

    if(togetherChars.isEmpty() && equalCharsNextMove.isEmpty()){
        println("All good")
    }

}

private fun checkMoveIsInLimit(
    x: Int,
    y: Int,
    move: MoveType
): Boolean {

    val xLimit = xCount - 1
    val yLimit = yCount - 1

    return when (move) {
        MoveType.Right -> x + 1 <= xLimit
        MoveType.Down -> y + 1 <= yLimit
        MoveType.Left -> x != 0
        MoveType.Top -> y != 0
    }
}

private fun checkMoveCanBeDone(
    x: Int,
    y: Int,
    move: MoveType,
    map: PMap,
): MoveCheckOutput {
    val coordinate = when (move) {
        MoveType.Right -> Coordinate(x + 1, y)
        MoveType.Down -> Coordinate(x, y + 1)
        MoveType.Left -> Coordinate(x - 1, y)
        MoveType.Top -> Coordinate(x, y - 1)
    }
    val disabledTypeForPlayers = setOf(CellType.Disabled, CellType.EnemyHome)
    val cell = map[coordinate]!!
    return MoveCheckOutput(
        canBeDone = !disabledTypeForPlayers.contains(cell.type),
        moveCell = cell
    )
}

data class MoveCheckOutput(
    val canBeDone: Boolean,
    val moveCell: Cell
)

enum class MoveType {
    Right,  // [x+1;y]
    Down,   // [x;y+1]
    Left,   // [x-1;y]
    Top,    // [x;y-1]
}

data class TogetherChars(
    val char: Char,
    val charCoordinate: Coordinate,
    val nextMoveChar: Char,
    val nextMoveCharCoordinate: Coordinate,
)

data class MoveThatCanBeDoneForChar(
    val move: MoveType,
    val moveCell: Cell
)
