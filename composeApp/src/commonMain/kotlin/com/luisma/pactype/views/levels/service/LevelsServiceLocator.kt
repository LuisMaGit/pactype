package com.luisma.pactype.views.levels.service

import com.luisma.pactype.services.levelsDBService
import com.luisma.pactype.services.platformService
import com.luisma.pactype.services.settingsDBService
import com.luisma.pactype.services.userProgressDBService

fun levelsService(): LevelsService {
    return LevelsService(
        userProgressDBService = userProgressDBService(),
        levelsDBService = levelsDBService(),
        settingsDBService = settingsDBService(),
        platformService = platformService()
    )
}

fun levelsKeyboardChannelService() : LevelsKeyboardChannelService {
    return LevelsKeyboardChannelService
}