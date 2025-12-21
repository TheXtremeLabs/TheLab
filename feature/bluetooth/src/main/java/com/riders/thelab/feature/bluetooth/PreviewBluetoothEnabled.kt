package com.riders.thelab.feature.bluetooth

import android.bluetooth.BluetoothDevice
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.flow.StateFlow

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun BluetoothEnabledContent(
    theme: AppTheme,
    darkTheme: Boolean,
    boundedDevices: Set<BluetoothDevice>,
    availableDevices: Set<BluetoothDevice>,
    isSearching: Boolean,
    uiEvent: (UiEvent) -> Unit,
) {
    val listState: LazyListState = rememberLazyListState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
                ) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Bluetooth enabled")

                            Button(
                                onClick = { uiEvent.invoke(UiEvent.OnStartDiscovery) },
                                enabled = !isSearching
                            ) {
                                Text(text = if (isSearching) "Searching ..." else "Search")

                            }
                        }
                    }

                    Text(modifier = Modifier.padding(vertical = 16.dp), text = "Paired Devices")
                }
            }

            itemsIndexed(items = boundedDevices.toList()) { index, item ->
                BluetoothItem(
                    theme = theme, darkTheme = darkTheme,
                    index = index,
                    bluetoothDevice = item,
                    totalItemsCount = boundedDevices.size
                )
            }

            if (isSearching || availableDevices.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = if (isSearching) "Searching ..." else "Available devices")

                        AnimatedVisibility(
                            modifier = Modifier.size(24.dp),
                            visible = isSearching
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }

                itemsIndexed(items = availableDevices.toList()) { index, item ->
                    BluetoothItem(
                        theme = theme, darkTheme = darkTheme,
                        index = index,
                        bluetoothDevice = item,
                        totalItemsCount = availableDevices.size
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
private fun PreviewPreviewBluetoothEnabled(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        BluetoothEnabledContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            boundedDevices = emptySet(),
            availableDevices = emptySet(),
            isSearching = false,
            uiEvent = {}
        )
    }
}