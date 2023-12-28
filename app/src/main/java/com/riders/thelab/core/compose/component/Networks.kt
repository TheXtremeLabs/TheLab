package com.riders.thelab.core.compose.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@DevicePreviews
@Composable
fun NetworkAvailable() = Text(
    text = stringResource(id = R.string.network_status_connected),
    color = Color.White,
    maxLines = 1
)

@DevicePreviews
@Composable
fun NetworkLost() = Text(
    text = stringResource(id = R.string.network_status_lost),
    color = Color.White,
    maxLines = 1
)

@DevicePreviews
@Composable
fun NetworkUnavailable() = Text(
    text = stringResource(id = R.string.network_status_disconnected),
    color = Color.White,
    maxLines = 1
)

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
