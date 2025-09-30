package com.luisma.pactype.views.levels.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.APPBAR_BTN_SIZE
import com.luisma.pactype.ui.components.Appbar
import com.luisma.pactype.ui.theme.LevelThemeCompose
import org.jetbrains.compose.resources.painterResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.ic_settings
@Composable
fun LevelAppbar(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose,
    onTapSettings: () -> Unit
) {
    Appbar(
        modifier = modifier.statusBarsPadding().navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Box(
            modifier = Modifier.clickable {
                onTapSettings()
            }
        ) {
            Image(
                modifier = Modifier.size(APPBAR_BTN_SIZE.dp),
                painter = painterResource(Res.drawable.ic_settings),
                colorFilter = ColorFilter.tint(color = levelTheme.textOn),
                contentDescription = "",
            )
        }
    }
}