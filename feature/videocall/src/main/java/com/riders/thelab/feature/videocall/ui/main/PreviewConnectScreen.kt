package com.riders.thelab.feature.videocall.ui.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.data.local.model.compose.Screen
import com.riders.thelab.feature.videocall.data.ConnectState
import kotlinx.serialization.Serializable
import kotools.types.text.toNotBlankString

@Serializable
data object ConnectRoute : Screen(route = "connect")

@Composable
fun ConnectScreen(
    theme: AppTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    username: String,
    connectState: ConnectState,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(
        theme = theme,
        darkTheme = darkTheme
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Connect Screen")

            TextField(
                value = username,
                onValueChange = { uiEvent(UiEvent.OnNameChanged(it)) },
                label = { Text("Name") },
                placeholder = { Text("Name") },
                isError = connectState.errorMessage != null,
                singleLine = true,
                maxLines = 1
            )

            Button(onClick = { uiEvent(UiEvent.OnConnectClicked) }) {
                Text(text = "Connect")
            }
        }
    }
}


@DevicePreviewsPhoneOnly
@Composable
private fun PreviewConnectScreen() {
    TheLabTheme(
        theme = AppTheme.Default,
        darkTheme = isSystemInDarkTheme(),
    ) {
        ConnectScreen(
            theme = AppTheme.Default,
            username = "Mike",
            connectState = ConnectState(
                name = "Mike".toNotBlankString().getOrThrow(),
                isConnected = false,
                errorMessage = null
            )
        ) {}
    }
}