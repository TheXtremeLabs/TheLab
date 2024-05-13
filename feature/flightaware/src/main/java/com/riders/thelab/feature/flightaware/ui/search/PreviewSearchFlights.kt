package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.SearchFlightsUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun SearchFlightsContent(
    currentDate: NotBlankString,
    uiState: SearchFlightsUiState,
    uiEvent: (UiEvent) -> Unit
) {
    val toolbarColor by animateColorAsState(
        targetValue = when (uiState) {
            is SearchFlightsUiState.Success -> cardBackgroundColor
            else -> backgroundColor
        },
        label = "toolbar_color_animation"
    )

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    navigationIconColor = Color.White,
                    withGradientBackground = false,
                    backgroundColor = toolbarColor
                )
            },
            containerColor = backgroundColor
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
                    label = "search_flight_animation_content"
                ) { targetState: SearchFlightsUiState ->
                    when (targetState) {
                        is SearchFlightsUiState.Loading -> {
                            LabLoader(modifier = Modifier.size(56.dp))
                        }

                        is SearchFlightsUiState.Error -> {
                            SearchFlightsErrorContent(reason = NotBlankString.create("Error occurred while getting value"))
                        }

                        is SearchFlightsUiState.Success -> {
                            SearchFlightsSuccessContent(
                                currentDate = currentDate,
                                flights = targetState.flights,
                                uiEvent = uiEvent
                            )
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
@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewSearchFlightsContent(@PreviewParameter(PreviewProviderSearchFlightsUiState::class) uiState: SearchFlightsUiState) {
    TheLabTheme {
        SearchFlightsContent(
            currentDate = NotBlankString.create("24/04/2024"),
            uiState = uiState
        ) {}
    }
}