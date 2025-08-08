package com.luisma.pactype.services

import com.luisma.pactype.platform.getAppVersion

class AppVersionService {
    fun version(): String {
        return getAppVersion()
    }
}