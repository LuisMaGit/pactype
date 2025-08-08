package com.luisma.pactype.services

import com.luisma.pactype.services.db_services.LevelsDBService
import com.luisma.pactype.services.db_services.SettingsDBService
import com.luisma.pactype.services.db_services.UserProgressDBService

fun levelsDBService(): LevelsDBService {
    return LevelsDBService()
}

fun userProgressDBService(): UserProgressDBService {
    return UserProgressDBService()
}

fun settingsDBService(): SettingsDBService {
    return SettingsDBService()
}

fun platformService(): PlatformService {
    return PlatformService()
}

fun appVersionService(): AppVersionService {
    return AppVersionService()
}

fun routerService(): RouterService {
    return RouterService
}

