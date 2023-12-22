package com.riders.thelab.core.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.success

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun NetworkStateContainer(icon: ImageVector, networkStateColor: Color, message: String) {

    val size: Dp = 30.dp
    val shape: Shape = CircleShape

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .border(1.dp, networkStateColor, shape)
                .size(size)
                .background(networkStateColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "wifi icon",
                tint = Color.White
            )
        }

        Text(modifier = Modifier.fillMaxWidth(), text = message, color = Color.White)
    }
}

@DevicePreviews
@Composable
fun NetworkAvailable() = NetworkStateContainer(
    icon = Icons.Filled.Wifi,
    networkStateColor = success,
    message = stringResource(id = R.string.network_status_connected)
)


@DevicePreviews
@Composable
fun NetworkLost() = NetworkStateContainer(
    icon = Icons.Filled.WifiOff,
    networkStateColor = com.riders.thelab.core.ui.compose.theme.error,
    message = stringResource(id = R.string.network_status_lost)
)


@DevicePreviews
@Composable
fun NetworkUnavailable() = NetworkStateContainer(
    icon = Icons.Filled.AirplanemodeActive,
    networkStateColor = Color.Gray,
    message = stringResource(id = R.string.network_status_disconnected)
)


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewNetworkStateContainer() {
    TheLabTheme {
        NetworkStateContainer(Icons.Filled.Wifi, Color.Gray, "Not connected")
    }
}
