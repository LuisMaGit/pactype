package com.luisma.pactype.views.settings_dialog

sealed class SettingsDialogsEvents {
    data object OnCreate : SettingsDialogsEvents()
    data object OnDispose : SettingsDialogsEvents()
    data class VirtualKeyboard(val on: Boolean) : SettingsDialogsEvents()
}