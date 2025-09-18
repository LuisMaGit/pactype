package com.luisma.pactype.services.db_services.db_models

data class LevelDBModel(
     val id: Int,
     val name: String,
     val mapType: Int,
     val charMap: String,
     val difficulty: Int,
     val levelDBColors: LevelDBColors
)