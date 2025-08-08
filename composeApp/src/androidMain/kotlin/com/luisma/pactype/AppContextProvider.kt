package com.luisma.pactype

import android.content.Context

// https://github.com/florent37/Multiplatform-Preferences/blob/master/src/androidMain/kotlin/com.github.florent37/preferences/AppContextProvider.kt
private var appContext: Context? = null

val application: Context
    get() = appContext ?: initAndGetAppCtxWithReflection()

private fun initAndGetAppCtxWithReflection(): Context {
    val activityThread = Class.forName("android.app.ActivityThread")
    val ctx = activityThread.getDeclaredMethod("currentApplication").invoke(null) as Context
    appContext = ctx
    return ctx
}