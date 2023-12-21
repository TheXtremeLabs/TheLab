package com.riders.thelab.core.ui.compose.utils

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@SuppressLint("NewApi")
fun Modifier.customBlur(blur: Float) = this.then(
    graphicsLayer {
        renderEffect = RenderEffect
            .createBlurEffect(
                blur,
                blur,
                Shader.TileMode.DECAL,
            )
            .asComposeRenderEffect()
    }
)

fun Modifier.angledGradientBackground(colors: List<Color>, degrees: Float) = this.then(
    drawBehind {
        /*
        Have to compute length of gradient vector so that it lies within
        the visible rectangle.
        --------------------------------------------
        | length of gradient ^  /                  |
        |             --->  /  /                   |
        |                  /  / <- rotation angle  |
        |                 /  o --------------------|  y
        |                /  /                      |
        |               /  /                       |
        |              v  /                        |
        --------------------------------------------
                             x

                   diagonal angle = atan2(y, x)
                 (it's hard to draw the diagonal)

        Simply rotating the diagonal around the centre of the rectangle
        will lead to points outside the rectangle area. Further, just
        truncating the coordinate to be at the nearest edge of the
        rectangle to the rotated point will distort the angle.
        Let α be the desired gradient angle (in radians) and γ be the
        angle of the diagonal of the rectangle.
        The correct for the length of the gradient is given by:
        x/|cos(α)|  if -γ <= α <= γ,   or   π - γ <= α <= π + γ
        y/|sin(α)|  if  γ <= α <= π - γ, or π + γ <= α <= 2π - γ
        where γ ∈ (0, π/2) is the angle that the diagonal makes with
        the base of the rectangle.

        */

        val (x, y) = size
        val gamma = atan2(y, x)

        if (gamma == 0f || gamma == (PI / 2).toFloat()) {
            // degenerate rectangle
            return@drawBehind
        }

        val degreesNormalised = (degrees % 360).let { if (it < 0) it + 360 else it }

        val alpha = (degreesNormalised * PI / 180).toFloat()

        val gradientLength = when (alpha) {
            // ray from centre cuts the right edge of the rectangle
            in 0f..gamma, in (2 * PI - gamma)..2 * PI -> {
                x / cos(alpha)
            }
            // ray from centre cuts the top edge of the rectangle
            in gamma..(PI - gamma).toFloat() -> {
                y / sin(alpha)
            }
            // ray from centre cuts the left edge of the rectangle
            in (PI - gamma)..(PI + gamma) -> {
                x / -cos(alpha)
            }
            // ray from centre cuts the bottom edge of the rectangle
            in (PI + gamma)..(2 * PI - gamma) -> {
                y / -sin(alpha)
            }
            // default case (which shouldn't really happen)
            else -> hypot(x, y)
        }

        val centerOffsetX = cos(alpha) * gradientLength / 2
        val centerOffsetY = sin(alpha) * gradientLength / 2

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                // negative here so that 0 degrees is left -> right and 90 degrees is top -> bottom
                start = Offset(center.x - centerOffsetX, center.y - centerOffsetY),
                end = Offset(center.x + centerOffsetX, center.y + centerOffsetY)
            ),
            size = size
        )
    }
)

fun Modifier.animatePlacement(): Modifier = composed {
    val scope = rememberCoroutineScope()
    var targetOffset by remember { mutableStateOf(IntOffset.Zero) }
    var animatable by remember {
        mutableStateOf<Animatable<IntOffset, AnimationVector2D>?>(null)
    }
    this
        // 🔥 onPlaced should be before offset Modifier
        .onPlaced {
            // Calculate the position in the parent layout
            targetOffset = it
                .positionInParent()
                .round()
        }
        .offset {
            // Animate to the new target offset when alignment changes.
            val anim = animatable ?: Animatable(targetOffset, IntOffset.VectorConverter)
                .also {
                    animatable = it
                }


            if (anim.targetValue != targetOffset) {
                scope.launch {
                    anim.animateTo(targetOffset, spring(stiffness = Spring.StiffnessMediumLow))
                }
            }
            // Offset the child in the opposite direction to the targetOffset, and slowly catch
            // up to zero offset via an animation to achieve an overall animated movement.
            animatable?.let { it.value - targetOffset } ?: IntOffset.Zero
        }
}
