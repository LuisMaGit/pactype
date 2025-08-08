package com.luisma.pactype.views.tutorial_dialog

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luisma.pactype.ui.components.BulletPointText
import com.luisma.pactype.ui.components.DialogWrapper
import com.luisma.pactype.ui.components.PText
import com.luisma.pactype.ui.theme.LevelThemeCompose
import com.luisma.pactype.ui.theme.P_FONT_SIZE_12
import org.jetbrains.compose.resources.stringResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.normal_mode
import pactype.composeapp.generated.resources.practice_mode
import pactype.composeapp.generated.resources.tutorial_how_to_play
import pactype.composeapp.generated.resources.tutorial_instruction
import pactype.composeapp.generated.resources.tutorial_normal_collect
import pactype.composeapp.generated.resources.tutorial_practice_collect
import pactype.composeapp.generated.resources.tutorial_practice_no_enemies

@Composable
fun TutorialDialog(
    modifier: Modifier = Modifier,
    onCloseDialog: () -> Unit,
    levelTheme: LevelThemeCompose,
) {


    DialogWrapper(
        modifier = modifier.height(350.dp),
        color = levelTheme.background,
        borderColor = levelTheme.textOn,
        headerButtonIconColor = levelTheme.textOn,
        onClose = onCloseDialog,
        titleColor = levelTheme.textOn,
        title = stringResource(Res.string.tutorial_how_to_play),
    ) {
        // type the map letters to move
        PText(
            modifier = Modifier.padding(bottom = 20.dp),
            text = stringResource(Res.string.tutorial_instruction),
            fontSize = P_FONT_SIZE_12.sp,
            color = levelTheme.textOn
        )
        // normal mode
        PText(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp),
            text = stringResource(Res.string.normal_mode),
            fontSize = P_FONT_SIZE_12.sp,
            color = levelTheme.main
        )
        BulletPointText(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 20.dp),
            colorBullet = levelTheme.main,
            colorText = levelTheme.textOn,
            text = stringResource(Res.string.tutorial_normal_collect)
        )
        // practice mode
        PText(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp),
            text = stringResource(Res.string.practice_mode),
            fontSize = P_FONT_SIZE_12.sp,
            color = levelTheme.main
        )
        BulletPointText(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 2.dp),
            colorBullet = levelTheme.main,
            colorText = levelTheme.textOn,
            text = stringResource(Res.string.tutorial_practice_collect)
        )
        BulletPointText(
            modifier = Modifier
                .align(Alignment.Start),
            colorBullet = levelTheme.main,
            colorText = levelTheme.textOn,
            text = stringResource(Res.string.tutorial_practice_no_enemies)
        )
    }
}
