package com.riders.thelab.core.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.data.local.model.compose.IslandState

@DevicePreviews
@Composable
fun TrailingContent(@PreviewParameter(IslandStatePreviewProvider::class) state: IslandState) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxHeight(),
        visible = state.hasTrailingContent,
        enter = fadeIn(animationSpec = tween(300, 300))
    ) {
        Box(
            Modifier.width(state.trailingContentSize),
            contentAlignment = Alignment.Center,
        ) {
            when (state) {
                is IslandState.CallState -> {
                    CallWaveform()
                }

                else -> {}
            }
        }
    }
}