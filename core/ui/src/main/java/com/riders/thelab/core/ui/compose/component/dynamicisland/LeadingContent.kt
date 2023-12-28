package com.riders.thelab.core.ui.compose.component.dynamicisland

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.core.ui.compose.theme.success

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
private fun NetworkStateContainer(icon: ImageVector, networkStateColor: Color) = Box(
    modifier = Modifier
        .clip(CircleShape)
        .border(1.dp, networkStateColor, CircleShape)
        .size(30.dp)
        .background(networkStateColor),
    contentAlignment = Alignment.Center
) {
    Icon(
        imageVector = icon,
        contentDescription = "wifi icon",
        tint = Color.White
    )
}


@DevicePreviews
@Composable
fun NetworkAvailable() = NetworkStateContainer(
    icon = Icons.Filled.Wifi,
    networkStateColor = success
)


@DevicePreviews
@Composable
fun NetworkLost() = NetworkStateContainer(
    icon = Icons.Filled.WifiOff,
    networkStateColor = com.riders.thelab.core.ui.compose.theme.error
)


@DevicePreviews
@Composable
fun NetworkUnavailable() = NetworkStateContainer(
    icon = Icons.Filled.AirplanemodeActive,
    networkStateColor = Color.Gray
)

@DevicePreviews
@Composable
fun LeadingContent(@PreviewParameter(IslandStatePreviewProvider::class) state: IslandState) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxHeight(),
        visible = state.hasLeadingContent,
        enter = fadeIn(animationSpec = tween(300, 300))
    ) {
        Box(
            Modifier.width(state.leadingContentSize),
            contentAlignment = Alignment.Center,
        ) {
            when (state) {
                is IslandState.CallState -> {
                    Text(text = "9:41", color = Green)
                }

                is IslandState.CallTimerState -> {
                    Icon(
                        imageVector = Icons.Rounded.Call,
                        contentDescription = null,
                        tint = Green,
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
    }
}