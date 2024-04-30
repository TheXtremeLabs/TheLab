package com.riders.thelab.feature.flightaware.ui.flight

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.FlightDetailUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun FlightDetailContent(uiState: FlightDetailUiState) {
    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    navigationIconColor = Color.White,
                    backgroundColor = backgroundColor
                )
            }
        ) { contentPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .background(color = backgroundColor)
            ) {
                AnimatedContent(
                    modifier = Modifier.size(width = this.maxWidth, height = this.maxHeight),
                    targetState = uiState,
                    contentAlignment = Alignment.Center,
                    label = "flight animation content"
                ) { targetState: FlightDetailUiState ->

                    when (targetState) {
                        is FlightDetailUiState.Loading -> {
                            LabLoader(modifier = Modifier.size(56.dp))
                        }

                        is FlightDetailUiState.Error -> {
                            FlightDetailErrorContent(reason = targetState.message)
                        }

                        is FlightDetailUiState.Success -> {
                            FlightDetailSuccessContent(flight = targetState.flight)
                        }
                    }
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
private fun PreviewFlightDetailContent(@PreviewParameter(PreviewProviderUiState::class) uiState: FlightDetailUiState) {
    TheLabTheme {
        FlightDetailContent(uiState = uiState)
    }
}

