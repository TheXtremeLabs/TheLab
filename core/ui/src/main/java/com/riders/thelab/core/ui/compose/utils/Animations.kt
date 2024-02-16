package com.riders.thelab.core.ui.compose.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut


/**
 * Creates a transitionSpec for configuring [AnimatedContentTransitionScope] to the fade through pattern.
 * See [Fade through](https://material.io/design/motion/the-motion-system.html#fade-through) for
 * the motion spec.
 */
fun fadeThrough(
    durationMillis: Int = 300
): AnimatedContentTransitionScope<Boolean>.() -> ContentTransform {
    return {
        ContentTransform(
            // The initial content fades out.
            initialContentExit = fadeOut(
                animationSpec = tween(
                    // The duration is 3/8 of the overall duration.
                    durationMillis = durationMillis * 3 / 8,
                    // FastOutLinearInEasing is suitable for elements exiting the screen.
                    easing = FastOutLinearInEasing
                )
            ),
            // The target content fades in after the current content fades out.
            targetContentEnter = fadeIn(
                animationSpec = tween(
                    // The duration is 5/8 of the overall duration.
                    durationMillis = durationMillis * 5 / 8,
                    // Delays the EnterTransition by the duration of the ExitTransition.
                    delayMillis = durationMillis * 3 / 8,
                    // LinearOutSlowInEasing is suitable for incoming elements.
                    easing = LinearOutSlowInEasing
                )
            ),
            // The size changes along with the content transition.
            sizeTransform = SizeTransform(
                sizeAnimationSpec = { _, _ ->
                    tween(durationMillis = durationMillis)
                }
            )
        )
    }
}
