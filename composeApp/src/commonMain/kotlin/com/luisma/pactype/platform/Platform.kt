package com.luisma.pactype.platform


enum class SupportedPlatforms {
    IOS,
    ANDROID,
    JVM
}


expect fun getPlatform(): SupportedPlatforms