package com.riders.thelab.feature.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun ShowMoreButton(viewModel: SettingsViewModel) {
    Button(onClick = { viewModel.updateShowMoreInfoOnDevice(!viewModel.showMoreInfoOnDevice) }) {
        AnimatedContent(
            targetState = viewModel.showMoreInfoOnDevice,
            label = "device_show_more_visibility_animation"
        ) { targetState ->
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (!targetState) "Show More" else "Close Panel")
                Icon(
                    imageVector = if (!targetState) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = "more icon"
                )
            }
        }
    }
}

@Composable
fun DeviceSpecs(viewModel: SettingsViewModel) {

    val deviceInfo = viewModel.deviceInfo!!

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = viewModel.showMoreInfoOnDevice
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = stringResource(id = com.riders.thelab.core.ui.R.string.device_specification_header),
                    style = Typography.titleMedium
                )

                // Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.weight(1f), text = "Device Name")
                    Text(modifier = Modifier.weight(1f), text = deviceInfo.name)
                }
            }
        }
        // Brand
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.weight(1f), text = "Brand")
            Text(modifier = Modifier.weight(1f), text = deviceInfo.brand)
        }
        // Model
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.weight(1f), text = "Model")
            Text(modifier = Modifier.weight(1f), text = deviceInfo.model)
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = viewModel.showMoreInfoOnDevice
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Hardware
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.weight(1f), text = "Hardware")
                    Text(modifier = Modifier.weight(1f), text = deviceInfo.hardware)
                }
                // Screen Dimension
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.weight(1f), text = "Screen Dimension")
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "${deviceInfo.screenWidth} x ${deviceInfo.screenHeight}"
                    )
                }
                // Serial
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.weight(1f), text = "Serial")
                    Text(modifier = Modifier.weight(1f), text = deviceInfo.serial)

                }
                // Fingerprint
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.weight(1f), text = "Fingerprint")
                    Text(modifier = Modifier.weight(1f), text = deviceInfo.fingerPrint)
                }
            }
        }
    }
}

@Composable
fun AndroidSpecs(viewModel: SettingsViewModel) {
    val deviceInfo = viewModel.deviceInfo!!

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = viewModel.showMoreInfoOnDevice
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = stringResource(id = com.riders.thelab.core.ui.R.string.device_android_specification_header),
                    style = Typography.titleMedium
                )
            }
        }
        // Name
        Row {
            Text(modifier = Modifier.weight(1f), text = "Name")
            Text(modifier = Modifier.weight(1f), text = deviceInfo.androidVersionName)

        }
        // Version
        Row {
            Text(modifier = Modifier.weight(1f), text = "Version")
            Text(modifier = Modifier.weight(1f), text = "${deviceInfo.sdkVersion}")
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = viewModel.showMoreInfoOnDevice
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Release
                Row {
                    Text(modifier = Modifier.weight(1f), text = "Release")
                    Text(modifier = Modifier.weight(1f), text = deviceInfo.androidRelease)

                }
                // Rooted
                Row {
                    Text(modifier = Modifier.weight(1f), text = "Rooted")
                    Text(modifier = Modifier.weight(1f), text = "${deviceInfo.rooted}")
                }
            }
        }
    }
}

@Composable
fun DeviceInfoSection(viewModel: SettingsViewModel) {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = "Device Info",
                style = Typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AnimatedContent(
                        modifier = Modifier.align(Alignment.TopCenter),
                        targetState = null != viewModel.deviceInfo,
                        label = "content_transition",
                    ) { target ->
                        if (!target) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator()
                                Text(text = "Fetching device's data. Please wait...")
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                )
                            ) {
                                // Device Specs
                                DeviceSpecs(viewModel)

                                // Android Specs
                                AndroidSpecs(viewModel)

                                ShowMoreButton(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewDeviceInfoSection() {
    val viewModel = hiltViewModel<SettingsViewModel>()
    TheLabTheme {
        DeviceInfoSection(viewModel)
    }
}
