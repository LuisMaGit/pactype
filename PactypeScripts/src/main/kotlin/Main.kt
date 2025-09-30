fun main(args: Array<String>) {
    val mapStr = getMapFileAsString("level_5")
    val map = buildGameMap(mapStr)
    checkAndPrintWrongCharsInGameMap(map.map)
}
