package com.riders.thelab.feature.bluetooth

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun BluetoothEnabledContent(viewModel: BluetoothViewModel) {
    val listState: LazyListState = rememberLazyListState()
    val boundedDevices by viewModel.boundedDevices.collectAsStateWithLifecycle()
    val availableDevices by viewModel.availableDevices.collectAsStateWithLifecycle()

    TheLabTheme {
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
                                onClick = { viewModel.startDiscovery() },
                                enabled = !viewModel.isSearching
                            ) {
                                Text(text = if (viewModel.isSearching) "Searching ..." else "Search")

                            }
                        }
                    }

                    Text(text = "Paired Devices")
                }
            }

            itemsIndexed(items = boundedDevices.toList()) { index, item ->
                BluetoothItem(
                    index = index,
                    bluetoothDevice = item,
                    totalItemsCount = boundedDevices.size
                )
            }

            if (viewModel.isSearching || availableDevices.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = if (viewModel.isSearching) "Searching ..." else "Available devices")

                        AnimatedVisibility(
                            modifier = Modifier.size(24.dp),
                            visible = viewModel.isSearching
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }

                itemsIndexed(items = availableDevices.toList()) { index, item ->
                    BluetoothItem(
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
private fun PreviewPreviewBluetoothEnabled() {
    val viewModel = BluetoothViewModel()

    TheLabTheme {
        BluetoothEnabledContent(viewModel = viewModel)
    }
}