package com.luisma.pactype.platform

actual fun getAppVersion(): String {
    return System.getProperty("jpackage.app-version") ?: "-dev"
}