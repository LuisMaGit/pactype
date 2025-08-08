package com.luisma.pactype.platform

import com.luisma.pactype.application

actual fun getAppVersion(): String {
    val packageManager = application.packageManager
    val packageName = application.packageName
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo.versionName
}