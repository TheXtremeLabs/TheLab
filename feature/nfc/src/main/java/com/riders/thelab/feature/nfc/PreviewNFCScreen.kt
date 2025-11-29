package com.riders.thelab.feature.nfc

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.nfc.NFCUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.color.warning
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.AppTypography
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography


///////////////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////////////
@Composable
fun NFCScreen(
    theme: AppTheme,
    darkTheme: Boolean,
    uiState: NFCUiState,
    isScanning: Boolean,
    isCustomMessageVisible: Boolean,
    customMessage: String,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {

                TheLabTopAppBar(
                    theme = theme,
                    toolbarSize = ToolbarSize.SMALL,
                    title = stringResource(R.string.app_name_nfc),
                    withGradientBackground = true,
                    actions = null
                )

            }
        ) { contentPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                AnimatedContent(
                    modifier = Modifier.size(this.maxWidth, this.maxHeight),
                    targetState = uiState
                ) { targetState ->
                    when (targetState) {
                        is NFCUiState.NotSupported -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Text(text = "Your device doesn't support NFC")

                                Lottie(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = this@BoxWithConstraints.maxHeight / 4),
                                    rawResId = com.riders.thelab.core.ui.R.raw.lottie_hot_coffee_loading
                                )

                                Button(onClick = {}) { Text("Quit") }
                            }
                        }

                        is NFCUiState.Disabled -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "NFC is disabled")
                                Button(onClick = { uiEvent.invoke(UiEvent.OpenSettings) }) { Text("Go To Settings") }
                            }
                        }

                        is NFCUiState.Enabled -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AnimatedVisibility(visible = isCustomMessageVisible) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(color = warning),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(16.dp),
                                            text = customMessage,
                                            color = Color.White,
                                            style = AppTypography.bodyLarge
                                        )
                                    }
                                }

                                Text(text = if (isScanning) "Scanning..." else "NFC is enabled")

                                Button(onClick = {
                                    uiEvent.invoke(
                                        if (!isScanning) {
                                            UiEvent.StartNFCScanning
                                        } else {
                                            UiEvent.StopNFCScanning
                                        }
                                    )
                                }) { Text(if (isScanning) "Stop" else "Start") }
                            }
                        }

                        is NFCUiState.Idle -> {
                            LabLoader(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(72.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


///////////////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewNFCScreen(@PreviewParameter(PreviewProviderNFCUiState::class) uiState: NFCUiState) {
    TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
        NFCScreen(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            uiState = uiState,
            isCustomMessageVisible = if (uiState is NFCUiState.Enabled) true else false,
            customMessage = if (uiState is NFCUiState.Enabled) "NFC is enabled" else "",
            isScanning = false
        ) {}
    }
}