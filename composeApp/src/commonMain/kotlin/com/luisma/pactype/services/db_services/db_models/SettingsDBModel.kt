package com.luisma.pactype.services.db_services.db_models

data class SettingsDBModel(
    val isFirstTime: Boolean,
    val showKeyboard: Boolean,
    val lastPlayedLevelId: Int? = null
)
