package com.riders.thelab.core.ui.compose.component.dynamicisland

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import kotlin.math.roundToInt


val bubbleEnterTransition = scaleIn(initialScale = .7f) + slideInHorizontally(
    animationSpec = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioLowBouncy,
    )
) { -it }

val bubbleExitTransition = scaleOut(targetScale = .7f) + slideOutHorizontally(
    animationSpec = spring(
        stiffness = Spring.StiffnessLow
    )
) { (-it * 1.2f).roundToInt() }