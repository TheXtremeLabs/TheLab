package com.riders.thelab.feature.bluetooth

import android.bluetooth.BluetoothDevice
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun BluetoothContent(
    theme: AppTheme,
    darkTheme: Boolean,
    isBluetoothEnabled: Boolean,
    boundedDevices: Set<BluetoothDevice>,
    availableDevices: Set<BluetoothDevice>,
    isSearching: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    title = stringResource(id = R.string.activity_bluetooth_title),
                    navigationIcon = {})
            }
        ) { contentPadding ->
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                targetState = isBluetoothEnabled,
                transitionSpec = { fadeIn() + slideInVertically() togetherWith slideOutHorizontally() + fadeOut() },
                label = ""
            ) { bluetoothTargetState ->
                if (!bluetoothTargetState) {
                    BluetoothDisabledContent(
                        theme = theme,
                        darkTheme = darkTheme,
                        isBluetoothEnabled = isBluetoothEnabled,
                        uiEvent = uiEvent
                    )
                } else {
                    BluetoothEnabledContent(
                        theme = theme,
                        darkTheme = darkTheme,
                        boundedDevices = boundedDevices,
                        availableDevices = availableDevices,
                        isSearching = isSearching,
                        uiEvent = uiEvent
                    )
                }
            }
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
private fun PreviewBluetoothContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        BluetoothContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            isBluetoothEnabled = true,
            boundedDevices = emptySet(),
            availableDevices = emptySet(),
            isSearching = false,
        ) {}
    }
}