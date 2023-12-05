package com.riders.thelab.feature.deviceinformation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.data.local.model.DeviceInformation
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography


@Composable
fun DeviceSpecs(deviceInfo: DeviceInformation) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = stringResource(id = R.string.placeholder_device_specs),
            style = Typography.titleLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(modifier = Modifier.weight(1f), text = "Device Name")
                    Text(modifier = Modifier.weight(1f), text = deviceInfo.name)
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
fun AndroidSpecs(deviceInfo: DeviceInformation) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = stringResource(id = R.string.placeholder_android_specs),
            style = Typography.titleLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Name
                Row() {
                    Text(modifier = Modifier.weight(1f), text = "Name")
                    Text(modifier = Modifier.weight(1f), text = deviceInfo.androidVersionName)

                }
                // Version
                Row() {
                    Text(modifier = Modifier.weight(1f), text = "Version")
                    Text(modifier = Modifier.weight(1f), text = "${deviceInfo.sdkVersion}")

                }
                // Release
                Row() {
                    Text(modifier = Modifier.weight(1f), text = "Release")
                    Text(modifier = Modifier.weight(1f), text = deviceInfo.androidRelease)

                }
                // Rooted
                Row() {
                    Text(modifier = Modifier.weight(1f), text = "Rooted")
                    Text(modifier = Modifier.weight(1f), text = "${deviceInfo.rooted}")

                }
            }
        }
    }
}

@Composable
fun DeviceInformationContent(viewModel: DeviceInformationViewModel) {

    val verticalScroll: ScrollState = rememberScrollState()

    TheLabTheme {
        Scaffold(
            topBar = { TheLabTopAppBar(title = stringResource(id = R.string.app_name_device_info)) {} }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.TopCenter),
                    targetState = null != viewModel.deviceInfo,
                    label = "content_transition",
                ) { target ->
                    if (!target) {
                        Column(
                            modifier = Modifier.padding(top = 72.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(text = "Fetching device's data. Please wait...")
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .verticalScroll(verticalScroll),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(
                                24.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            // Device Specs
                            DeviceSpecs(deviceInfo = viewModel.deviceInfo!!)

                            // Android Specs
                            AndroidSpecs(deviceInfo = viewModel.deviceInfo!!)
                        }
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewDeviceSpecs(@PreviewParameter(PreviewProviderDeviceInformation::class) deviceInfo: DeviceInformation) {
    TheLabTheme {
        DeviceSpecs(deviceInfo = deviceInfo)
    }
}

@DevicePreviews
@Composable
private fun PreviewAndroidSpecs(@PreviewParameter(PreviewProviderDeviceInformation::class) deviceInfo: DeviceInformation) {
    TheLabTheme {
        AndroidSpecs(deviceInfo = deviceInfo)
    }
}

@DevicePreviews
@Composable
private fun PreviewDeviceInformationContent() {
    val viewModel: DeviceInformationViewModel = hiltViewModel()
    TheLabTheme {
        DeviceInformationContent(viewModel = viewModel)
    }
}