package com.riders.thelab.feature.nfc

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.nfc.NFCUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.color.warning
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.component.fab.PulsarFabWithClick
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.AppTypography
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity


///////////////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////////////
@Composable
fun NFCDisabledContent(
    theme: AppTheme,
    darkTheme: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(modifier = Modifier.padding(16.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "NFC is disabled",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Go to settings in order to enable the NFC"
                    )
                    Button(onClick = { uiEvent.invoke(UiEvent.OpenSettings) }) { Text("Go To Settings") }
                }
            }
        }
    }
}

@Composable
fun NFCPulsarButton(
    theme: AppTheme,
    darkTheme: Boolean,
    isPulsing: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val pulsarContainerColor: Color by animateColorAsState(
        targetValue = if (isPulsing) MaterialTheme.colorScheme.primary else CardDefaults.cardColors().containerColor
    )
    val pulsarElevation: Dp by animateDpAsState(
        targetValue = if (!isPulsing) 4.dp else 0.dp
    )

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            PulsarFabWithClick(
                isPulsing = isPulsing,
                containerColor = pulsarContainerColor,
                elevation = pulsarElevation,
                onClick = onClick
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Transparent),
                        painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.ic_nfc_black),
                        contentDescription = null
                    )
                }
            }
        }
    }
}


@Composable
fun NFCEnabledContent(
    theme: AppTheme,
    darkTheme: Boolean,
    isScanning: Boolean,
    isCustomMessageVisible: Boolean,
    customMessage: String,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
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

            Card(modifier = Modifier.padding(16.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "NFC is enabled",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = if (isScanning) "Scanning..." else "Click here, on the NFC icon button to start the NFC scanner"
                    )

                    NFCPulsarButton(
                        theme = theme,
                        darkTheme = darkTheme,
                        isPulsing = isScanning,
                        onClick = {
                            uiEvent.invoke(
                                if (!isScanning) {
                                    UiEvent.StartNFCScanning
                                } else {
                                    UiEvent.StopNFCScanning
                                }
                            )
                        }
                    )
                }
            }

        }
    }
}

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
    val context = LocalContext.current
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp
    val screenHeight = LocalWindowInfo.current.containerSize.height.dp


    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
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
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                targetState = uiState,
                contentAlignment = Alignment.TopCenter
            ) { targetState ->
                when (targetState) {
                    is NFCUiState.NotSupported -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Text(text = "Your device doesn't support NFC")

                            Lottie(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(screenHeight / 6),
                                rawResId = com.riders.thelab.core.ui.R.raw.lottie_hot_coffee_loading
                            )

                            Button(onClick = { (context.findActivity() as NFCActivity).backPressed() }) {
                                Text(
                                    "Quit"
                                )
                            }
                        }
                    }

                    is NFCUiState.Disabled -> {
                        NFCDisabledContent(
                            theme = theme,
                            darkTheme = darkTheme,
                            uiEvent = uiEvent
                        )
                    }

                    is NFCUiState.Enabled -> {
                        NFCEnabledContent(
                            theme = theme,
                            darkTheme = darkTheme,
                            isScanning = isScanning,
                            isCustomMessageVisible = isCustomMessageVisible,
                            customMessage = customMessage,
                            uiEvent = uiEvent
                        )
                    }

                    is NFCUiState.Idle -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
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
private fun PreviewNFCDisabledContent() {
    TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
        NFCDisabledContent(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {}
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewNFCPulsarButton() {
    TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
        NFCPulsarButton(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            isPulsing = false
        )
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewNFCEnabledContent() {
    TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
        NFCEnabledContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            isScanning = false,
            isCustomMessageVisible = false,
            customMessage = "",
            uiEvent = {}
        )
    }
}


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