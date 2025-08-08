package com.luisma.pactype.services

import com.luisma.pactype.platform.SupportedPlatforms
import com.luisma.pactype.platform.getPlatform

class PlatformService {
    fun platform() : SupportedPlatforms {
        return getPlatform()
    }
}