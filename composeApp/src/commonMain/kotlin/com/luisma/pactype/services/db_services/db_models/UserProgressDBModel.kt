package com.luisma.pactype.services.db_services.db_models

data class UserProgressDBModel(
    val levelId: Int,
    val playModeBestTimeCS: Int,
    val playModeProgress: Int,
    val practiceModeBestTimeCS: Int
) {
    companion object {
        fun empty(): UserProgressDBModel {
            return UserProgressDBModel(
                levelId = -1,
                playModeBestTimeCS = 0,
                playModeProgress = 0,
                practiceModeBestTimeCS = 0,
            )
        }
    }
}