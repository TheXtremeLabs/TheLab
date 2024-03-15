package com.riders.thelab.core.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Welcome
import com.riders.thelab.core.ui.compose.component.dynamicisland.FaceUnlock
import com.riders.thelab.core.ui.compose.component.dynamicisland.LeadingContent
import com.riders.thelab.core.ui.compose.component.dynamicisland.TrailingContent
import com.riders.thelab.core.ui.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.ui.mainactivity.MainActivityViewModel


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun IslandContent(
    state: IslandState,
    searchedAppRequest: String,
    onSearchAppRequestChanged: (String) -> Unit,
    isMicrophoneEnabled: Boolean,
    onUpdateMicrophoneEnabled: (Boolean) -> Unit,
    onUpdateKeyboardVisible: (Boolean) -> Unit
) {

    val width by animateDpAsState(
        targetValue = state.fullWidth,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = .6f,
        ),
        label = "width animation"
    )

    val height by animateDpAsState(
        targetValue = state.contentSize.height,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = .6f,
        ),
        label = "height animation"
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
                        Search(
                            dynamicIslandState = state,
                            searchedAppRequest = searchedAppRequest,
                            onSearchAppRequestChanged = onSearchAppRequestChanged,
                            isMicrophoneEnabled = isMicrophoneEnabled,
                            onUpdateMicrophoneEnabled = onUpdateMicrophoneEnabled,
                            onUpdateKeyboardVisible = onUpdateKeyboardVisible
                        )
                    }

                    is IslandState.NetworkState.Available -> {
                        NetworkAvailable()
                    }

                    is IslandState.NetworkState.Lost -> {
                        NetworkLost()
                    }

                    is IslandState.NetworkState.Unavailable -> {
                        NetworkUnavailable()
                    }

                    else -> {}
                }
            }
            TrailingContent(state)
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
fun PreviewIslandContent(@PreviewParameter(IslandStatePreviewProvider::class) state: IslandState) {

    IslandContent(
        state = state,
        searchedAppRequest = "Colors",
        onSearchAppRequestChanged = {},
        isMicrophoneEnabled = false,
        onUpdateMicrophoneEnabled = {},
        onUpdateKeyboardVisible = {}
    )
}
