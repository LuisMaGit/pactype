package com.luisma.pactype.views.levels.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.theme.LevelThemeCompose
import org.jetbrains.compose.resources.painterResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.ic_arrow

val LEVEL_ARROW_H = 36.dp

@Composable
fun LevelArrows(
    modifier: Modifier = Modifier,
    levelTheme: LevelThemeCompose,
    showPrevious: Boolean,
    showNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    val sizeModifier = Modifier.width(22.dp).height(LEVEL_ARROW_H)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showPrevious) {
            Box(
                modifier = Modifier
                    .clickable { onPrevious() }
            ) {
                Image(
                    modifier = sizeModifier,
                    painter = painterResource(Res.drawable.ic_arrow),
                    colorFilter = ColorFilter.tint(color = levelTheme.textOn),
                    contentDescription = "",
                )
            }
        } else {
            Box(sizeModifier)
        }
        if (showNext) {
            Box(
                modifier = Modifier
                    .clickable { onNext() }
            ) {
                Image(
                    modifier = sizeModifier.rotate(180f),
                    painter = painterResource(Res.drawable.ic_arrow),
                    colorFilter = ColorFilter.tint(color = levelTheme.textOn),
                    contentDescription = "",
                )
            }
        } else {
            Box(sizeModifier)
        }
    }
}