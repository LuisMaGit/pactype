import java.io.File

fun printReversedPattern(pattern: String) {
    print(pattern.split("").reversed().joinToString(separator = "") { it })
}

fun printFullStats() {
    println("\nmap1")
    printPatternStats(getMapFileAsString("map1"))
    println("\nmap2")
    printPatternStats(getMapFileAsString("map2"))
    println("\nmap3")
    printPatternStats(getMapFileAsString("map3"))
    println("\nmap4")
    printPatternStats(getMapFileAsString("map4"))
    println("\nmap5")
    printPatternStats(getMapFileAsString("map5"))
}

fun printPatternStats(pattern: String) {
    var letterCount = 0
    var disabledCount = 0
    var spaceCount = 0
    for (p in pattern) {
        if (disabledCharsForPlayer.contains(p)) {
            disabledCount++
        } else if (p == spaceChar) {
            spaceCount++
        } else if (p != '\n' && p != ' ') {
            letterCount++
        }
    }

    println("Letters: $letterCount")
    println("Spaces: $spaceCount")
    println("Disabled: $disabledCount")
    println("Total: ${disabledCount + letterCount + spaceCount}")
}

fun getMapFileAsString(map: String): String {
    val buffered = File(map).bufferedReader()
    return buffered.readText()
}

fun buildGameMap(mapStr: String): GameMapData {
    val map = mutableMapOf<Coordinate, Cell>()
    var x = 0
    var y = 0
    var disabledCount = 0
    var lettersCount = 0
    var spaceCount = 0

    for (char in mapStr) {
        if (char == '\n') {
            y++
            x = 0
        } else {
            val cellType = when (char) {
                disabledChar -> {
                    disabledCount++
                    CellType.Disabled
                }

                enemyHomeChar -> {
                    disabledCount++
                    CellType.EnemyHome
                }

                spaceChar -> {
                    spaceCount++
                    CellType.Space
                }

                else -> {
                    lettersCount++
                    CellType.Letter
                }
            }
            map[Coordinate(x, y)] = Cell(
                x = x,
                y = y,
                type = cellType,
                char = char,
                letterIntersection = false,
                letterDone = false
            )
            x++
        }
    }

    return GameMapData(
        map = map,
        disabledCount = disabledCount,
        lettersCount = lettersCount,
        spaceCount = spaceCount
    )
}

