package com.riders.thelab.core.ui.compose.component.dynamicisland

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.core.ui.compose.theme.Orange

@DevicePreviews
@Composable
fun IslandBubbleContent(@PreviewParameter(IslandStatePreviewProvider::class) state: IslandState) {
    val width = state.bubbleContentSize.width
    val height = state.bubbleContentSize.height

    val scale = remember { Animatable(1.5f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = 0.35f,
            )
        )
    }

    Box(
        modifier = Modifier
            .width(width * scale.value)
            .height(height),
        contentAlignment = Alignment.Center,
    ) {
        var bubbleContent: @Composable () -> Unit by remember { mutableStateOf({}) }

        LaunchedEffect(state, block = {
            when (state) {
                is IslandState.CallTimerState -> {
                    bubbleContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.timer),
                            contentDescription = null,
                            tint = Orange
                        )
                    }
                }

                else -> {}
            }
        })
        bubbleContent()
    }
}