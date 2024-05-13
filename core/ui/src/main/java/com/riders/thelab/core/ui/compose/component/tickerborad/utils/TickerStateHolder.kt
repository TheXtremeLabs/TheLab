package com.riders.thelab.core.ui.compose.component.tickerborad.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable

private const val TickerCycleMillis = 50

@Stable
class TickerStateHolder {
    private val animatable = Animatable(0f)

    val value: Float
        get() = animatable.value

    val index: Int
        get() = animatable.value.toInt()

    suspend fun animateTo(target: TickerIndex) {
        val currentIndex = animatable.value.toInt()
        val result = animatable.animateTo(
            targetValue = target.offsetIndex.toFloat(),
            animationSpec = tween(
                durationMillis = (target.offsetIndex - currentIndex) * TickerCycleMillis,
                easing = FastOutSlowInEasing,
            )
        )
        if (result.endReason == AnimationEndReason.Finished) {
            snapTo(target.index)
        }
    }

    private suspend fun snapTo(index: Int) {
        animatable.snapTo(index.toFloat())
    }
}