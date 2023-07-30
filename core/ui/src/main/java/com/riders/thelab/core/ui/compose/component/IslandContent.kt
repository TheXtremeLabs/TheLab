package com.riders.thelab.core.ui.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.data.local.model.compose.IslandState
import com.riders.thelab.ui.mainactivity.MainActivityViewModel

@Composable
fun IslandContent(
    viewModel: MainActivityViewModel,
    @PreviewParameter(IslandStatePreviewProvider::class) state: IslandState
) {

    val width by animateDpAsState(
        targetValue = state.fullWidth,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = .6f,
        )
    )

    val height by animateDpAsState(
        targetValue = state.contentSize.height,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = .6f,
        )
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {

        AnimatedVisibility(
            visible = state.hasMainContent,
            enter = fadeIn(
                animationSpec = tween(300, 300)
            )
        ) {
            Box(
                modifier = Modifier.size(state.contentSize)
            ) {
                when (state) {
                    is IslandState.FaceUnlockState -> {
                        FaceUnlock()
                    }

                    else -> {}
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LeadingContent(state)
            Box(Modifier.weight(1f)) {
                when (state) {
                    is IslandState.WelcomeState -> {
                        Welcome()
                    }

                    is IslandState.SearchState -> {
                        Search(viewModel)
                    }

                    else -> {}
                }
            }
            TrailingContent(state)
        }
    }
}


@DevicePreviews
@Composable
fun PreviewIslandContent() {
    val viewModel: MainActivityViewModel = hiltViewModel()
    IslandContent(viewModel, IslandStatePreviewProvider().values.first())
}