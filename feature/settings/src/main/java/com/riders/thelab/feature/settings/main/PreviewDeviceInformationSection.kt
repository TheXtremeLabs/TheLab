package com.riders.thelab.feature.settings.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.DeviceInformation
import com.riders.thelab.core.data.local.model.compose.settings.DeviceInfoUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.feature.settings.R
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun ShowMoreButton(
    theme: AppTheme,
    darkTheme: Boolean,
    showMoreInfoOnDevice: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Button(onClick = { uiEvent.invoke(UiEvent.OnUpdateShowMoreInfoOnDevice(!showMoreInfoOnDevice)) }) {
            AnimatedContent(
                targetState = showMoreInfoOnDevice,
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
}

@Composable
fun DeviceSpecs(deviceInfo: DeviceInformation, showMoreInfoOnDevice: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = showMoreInfoOnDevice
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = stringResource(id = com.riders.thelab.core.ui.R.string.device_specification_header),
                    style = Typography.titleMedium, color = MaterialTheme.colorScheme.onSurface
                )

                // Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Device Name",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = deviceInfo.name,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        // Brand
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Brand",
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.weight(1f),
                text = deviceInfo.brand,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        // Model
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Model",
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.weight(1f),
                text = deviceInfo.model,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = showMoreInfoOnDevice
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
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Hardware",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = deviceInfo.hardware,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Screen Dimension
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Screen Dimension",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "${deviceInfo.screenWidth} x ${deviceInfo.screenHeight}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                // Serial
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Serial",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = deviceInfo.serial,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }
                // Fingerprint
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Fingerprint",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = deviceInfo.fingerPrint,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun AndroidSpecs(deviceInfo: DeviceInformation, showMoreInfoOnDevice: Boolean) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = showMoreInfoOnDevice
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = stringResource(id = com.riders.thelab.core.ui.R.string.device_android_specification_header),
                    style = Typography.titleMedium, color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        // Name
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = "Name",
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.weight(1f),
                text = deviceInfo.androidVersionName,
                color = MaterialTheme.colorScheme.onSurface
            )

        }
        // Version
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = "Version",
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(
                    id = R.string.placeholder_android_version,
                    deviceInfo.sdkVersion
                ), color = MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = showMoreInfoOnDevice
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Release
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Release",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = deviceInfo.androidRelease,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }
                // Rooted
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Rooted",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "${deviceInfo.rooted}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun DeviceInfoSection(
    theme: AppTheme,
    darkTheme: Boolean,
    deviceInformationUiState: DeviceInfoUiState,
    showModeInfo: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        SettingsSectionWithTitle(
            theme = theme,
            darkTheme = darkTheme,
            title = NotBlankString.create(stringResource(id = R.string.placeholder_device_information))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.TopCenter),
                    targetState = deviceInformationUiState,
                    label = "content_transition",
                ) { targetState ->

                    when (targetState) {
                        is DeviceInfoUiState.Loading -> {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text = "Fetching device's data. Please wait...",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        is DeviceInfoUiState.Error -> {}
                        is DeviceInfoUiState.Success -> {
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
                                DeviceSpecs(
                                    deviceInfo = targetState.deviceInformation,
                                    showMoreInfoOnDevice = showModeInfo
                                )

                                // Android Specs
                                AndroidSpecs(
                                    deviceInfo = targetState.deviceInformation,
                                    showMoreInfoOnDevice = showModeInfo
                                )

                                ShowMoreButton(
                                    theme = theme, darkTheme = darkTheme,
                                    showMoreInfoOnDevice = showModeInfo,
                                    uiEvent = uiEvent
                                )
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
private fun PreviewDeviceInfoSection(@PreviewParameter(PreviewProviderDeviceInfoUiState::class) deviceUiState: DeviceInfoUiState) {
    TheLabTheme(theme = AppTheme.Red) {
        DeviceInfoSection(
            theme = AppTheme.Red,
            darkTheme = isSystemInDarkTheme(),
            deviceInformationUiState = deviceUiState,
            showModeInfo = true
        ) {}
    }
}
