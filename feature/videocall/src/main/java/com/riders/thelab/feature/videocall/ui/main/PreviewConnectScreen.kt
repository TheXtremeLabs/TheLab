package com.riders.thelab.feature.videocall.ui.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.textfield.LabOutlinedTextField
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.data.local.model.compose.Screen
import com.riders.thelab.feature.videocall.data.ConnectState
import kotlinx.serialization.Serializable

@Serializable
data object ConnectRoute : Screen(route = "connect")

@Composable
fun ConnectScreen(
    theme: AppTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
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
            Text(text = "Connect Screen", color = if (!darkTheme) Color.Black else Color.White)

            LabOutlinedTextField(
                theme = theme,
                modifier = Modifier.fillMaxWidth(),
                query = connectState.name,
                onUpdateQuery = { newValue -> uiEvent(UiEvent.OnNameChanged(newValue)) },
                label = "Name",
                placeholder = "Name",
                onOutsideBoundariesClicked = true,
                errorMessage = connectState.errorMessage,
                shape = RoundedCornerShape(35)
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
        Surface(modifier = Modifier.fillMaxSize()) {
            ConnectScreen(
                theme = AppTheme.Default,
                connectState = ConnectState(
                    name = "Mike",
                    isConnected = false,
                    errorMessage = null
                )
            ) {}
        }
    }
}