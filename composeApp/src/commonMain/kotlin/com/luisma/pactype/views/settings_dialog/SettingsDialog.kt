package com.luisma.pactype.views.settings_dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luisma.pactype.ui.components.DialogWrapper
import com.luisma.pactype.ui.components.IconTitle
import com.luisma.pactype.ui.components.PButton
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import org.jetbrains.compose.resources.stringResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.ic_keyboard
import pactype.composeapp.generated.resources.ic_question
import pactype.composeapp.generated.resources.settings_how_play
import pactype.composeapp.generated.resources.settings_off
import pactype.composeapp.generated.resources.settings_on
import pactype.composeapp.generated.resources.settings_title
import pactype.composeapp.generated.resources.settings_virtual_keyboard

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose,
    state: SettingsDialogState,
    sendEvent: (event: SettingsDialogsEvents) -> Unit,
    onCloseDialog: () -> Unit,
    onOpenTutorial: ()-> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        sendEvent(SettingsDialogsEvents.OnCreate)
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            sendEvent(SettingsDialogsEvents.OnDispose)
        }
    }

    DialogWrapper(
        modifier = modifier,
        color = levelTheme.background,
        borderColor = levelTheme.textOn,
        headerButtonIconColor = levelTheme.textOn,
        onClose = onCloseDialog,
        titleColor = levelTheme.textOn,
        title = stringResource(Res.string.settings_title),
    ) {
        // virtual keyboard
        IconTitle(
            modifier = Modifier.padding(bottom = 4.dp).align(Alignment.Start),
            icon = Res.drawable.ic_keyboard,
            colorIcon = levelTheme.textOff,
            text = stringResource(Res.string.settings_virtual_keyboard),
            colorText = levelTheme.textOn,
            iconSize = 20.dp
        )
        // virtual keyboard buttons
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 24.dp)
        ) {
            PButton(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clickable {
                        sendEvent(SettingsDialogsEvents.VirtualKeyboard(on = true))
                    },
                text = stringResource(Res.string.settings_on),
                levelTheme = levelTheme,
                enabled = state.keyboardOn,
            )
            PButton(
                modifier = Modifier
                    .clickable {
                        sendEvent(SettingsDialogsEvents.VirtualKeyboard(on = false))
                    },
                text = stringResource(Res.string.settings_off),
                levelTheme = levelTheme,
                enabled = !state.keyboardOn,
            )
        }
        // how to play
        IconTitle(
            modifier = Modifier.align(Alignment.Start).clickable {
                onOpenTutorial()
            },
            icon = Res.drawable.ic_question,
            colorIcon = levelTheme.textOff,
            text = stringResource(Res.string.settings_how_play),
            colorText = levelTheme.textOn,
            iconSize = 16.dp,
        )
        Spacer(modifier = Modifier.weight(1f))
        // version
        PText(
            modifier = Modifier.padding(bottom = 16.dp),
            text = "v${state.version}",
            fontSize = P_FONT_SIZE_12.sp,
            color = levelTheme.textOff,
        )
    }
}