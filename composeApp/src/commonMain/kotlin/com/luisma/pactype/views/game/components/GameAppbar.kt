package com.luisma.pactype.views.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.APPBAR_BTN_SIZE
import com.luisma.pactype.ui.components.Appbar
import com.luisma.pactype.ui.theme.LevelTheme
import com.luisma.pactype.ui.theme.LevelThemeCompose
import org.jetbrains.compose.resources.painterResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.ic_arrow
import pactype.composeapp.generated.resources.ic_pause

@Composable
fun GameAppbar(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose,
    onPause: () -> Unit,
    onBack: () -> Unit,
    showPause: Boolean,
    showBack: Boolean,
) {
    Appbar(
        modifier = modifier,
    ) {
        if (showBack) {
            Box(
                modifier = Modifier.clickable {
                    onBack()
                }
            ) {
                Image(
                    modifier = Modifier.size(APPBAR_BTN_SIZE.dp),
                    painter = painterResource(Res.drawable.ic_arrow),
                    colorFilter = ColorFilter.tint(color = levelTheme.textOn),
                    contentDescription = "",
                )
            }
        }
        Spacer(modifier = Modifier.weight(1F))
        if (showPause) {
            Box(
                modifier = Modifier.clickable {
                    onPause()
                }
            ) {
                Image(
                    modifier = Modifier.size(APPBAR_BTN_SIZE.dp),
                    painter = painterResource(Res.drawable.ic_pause),
                    colorFilter = ColorFilter.tint(color = levelTheme.textOn),
                    contentDescription = "",
                )
            }
        }
    }

}