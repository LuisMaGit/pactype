package com.luisma.pactype.views.levels.data

enum class LevelsDifficulty(val dbValue: Int) {
    Easy(dbValue = 1),
    Mid(dbValue = 2),
    Hard(dbValue = 3), ;

    companion object {
        fun fromDB(value: Int): LevelsDifficulty {
            return entries.firstOrNull { it.dbValue == value }
                ?: Easy
        }
    }

}