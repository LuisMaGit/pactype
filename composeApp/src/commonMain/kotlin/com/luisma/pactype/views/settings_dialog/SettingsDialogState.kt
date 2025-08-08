package com.luisma.pactype.views.settings_dialog

data class SettingsDialogState(
    val keyboardOn: Boolean,
    val dialogOpened: Boolean,
    val version: String,
) {
    companion object {
        fun initial() : SettingsDialogState {
            return SettingsDialogState(
                keyboardOn = false,
                dialogOpened = false,
                version = ""
            )
        }
    }
}