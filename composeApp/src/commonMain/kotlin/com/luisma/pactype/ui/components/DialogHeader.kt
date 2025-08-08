package com.luisma.pactype.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.theme.DefaultColors
import org.jetbrains.compose.resources.painterResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.ic_close

@Composable
fun DialogHeader(
    modifier: Modifier = Modifier,
    buttonIconColor: Color? = null,
    onClose: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .padding(
                top = 16.dp,
                bottom = 12.dp
            )
    ) {
        if (onClose != null) {
            Spacer(modifier = Modifier.weight(1F))
            Box(
                modifier = Modifier.clickable {
                    onClose()
                }
            ) {
                Image(
                    modifier = Modifier
                        .size(CIRCLE_ICON_BTN_SMALL_ICON_SIZE.dp),
                    painter = painterResource(Res.drawable.ic_close),
                    colorFilter = ColorFilter.tint(color = buttonIconColor ?: DefaultColors.textOn),
                    contentDescription = "",
                )
            }
        }
    }
}