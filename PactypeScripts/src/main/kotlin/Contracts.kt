const val disabledChar = '*'
const val enemyHomeChar = '='
const val spaceChar = '␣'

val disabledCharsForPlayer = listOf(disabledChar, enemyHomeChar)

const val xCount = 34
const val yCount = 31

data class Cell(
    val x: X,
    val y: Y,
    val type: CellType,
    val char: Char,
    val letterIntersection: Boolean,
    val letterDone: Boolean
)

enum class CellType {
    Disabled,
    Letter,
    Space,
    EnemyHome,
}

typealias X = Int
typealias Y = Int
typealias Coordinate = Pair<X, Y>
typealias PMap = Map<Coordinate, Cell>

data class GameMapData(
    val map: PMap,
    val disabledCount: Int,
    val lettersCount: Int,
    val spaceCount: Int
)

