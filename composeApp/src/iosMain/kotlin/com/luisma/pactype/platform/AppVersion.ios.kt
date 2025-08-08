package com.luisma.pactype.platform

import platform.Foundation.NSBundle


actual fun getAppVersion(): String {
    return NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString")?.toString() ?: "unknown"
}