package com.luisma.pactype.views.game.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.luisma.pactype.views.game.data.LOST_SHAKE_ANIMATION_DURATION_MS
import com.luisma.pactype.views.game.states.GameStatus
import com.luisma.pactype.views.game.states.GameStatusState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pactype.composeapp.generated.resources.Res
import pactype.composeapp.generated.resources.map_1
import pactype.composeapp.generated.resources.map_2
import pactype.composeapp.generated.resources.map_3
import pactype.composeapp.generated.resources.map_4
import pactype.composeapp.generated.resources.map_5
import kotlin.math.pow
import kotlin.math.sin

@Composable
fun MapImage(
    modifier: Modifier = Modifier,
    color: Color,
    mapType: Int,
    statusState: GameStatusState
) {

    fun mapType(): DrawableResource {
        return when (mapType) {
            1 -> Res.drawable.map_1
            2 -> Res.drawable.map_2
            3 -> Res.drawable.map_3
            4 -> Res.drawable.map_4
            5 -> Res.drawable.map_5
            else -> Res.drawable.map_1
        }
    }

    val triggerShakeAnimation = statusState.status == GameStatus.Lost
    val easingShakeAnimation = FastOutSlowInEasing

    fun fadingWave(
        x: Int,
        c1: Double, c2: Double, p1: Double,
        c3: Double, c4: Double, p2: Double,
        c5: Double, c6: Double, p3: Double
    ): Double {
        val t = LOST_SHAKE_ANIMATION_DURATION_MS.toDouble()
        val n = 2.0
        val envelope = (1 - x / t).pow(n)
        val wave = c1 * sin(c2 * x + p1) + c3 * sin(c4 * x + p2) + c5 * sin(c6 * x + p3)
        return wave * envelope
    }


    val offsetX by animateDpAsState(
        targetValue = if (triggerShakeAnimation) {
            fadingWave(
                statusState.lostShakeAnimationFactor,
                c1 = 5.0, c2 = 5.0, p1 = 6.0,
                c3 = 2.0, c4 = 3.0, p2 = 8.0,
                c5 = 7.0, c6 = 2.0, p3 = 9.0
            ).dp
        } else {
            0.dp
        },
        animationSpec = tween(
            durationMillis = 1,
            easing = easingShakeAnimation
        )
    )

    val offsetY by animateDpAsState(
        targetValue = if (triggerShakeAnimation) {
            fadingWave(
                statusState.lostShakeAnimationFactor,
                c1 = 3.0, c2 = 2.0, p1 = 9.0,
                c3 = 8.0, c4 = 5.0, p2 = 2.0,
                c5 = 2.0, c6 = 3.0, p3 = 8.0
            ).dp
        } else {
            0.dp
        },
        animationSpec = tween(
            durationMillis = 1,
            easing = easingShakeAnimation
        )
    )


    Image(
        modifier = modifier.offset(
            x = offsetX,
            y = offsetY
        ),
        painter = painterResource(mapType()),
        colorFilter = ColorFilter.tint(color = color),
        contentDescription = "map_$mapType",
        contentScale = ContentScale.Fit
    )
}