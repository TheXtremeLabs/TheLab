package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.data.local.model.flight.SegmentModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.ui.flight.FlightInfoContainer
import com.riders.thelab.feature.flightaware.ui.flight.FlightStatusCard
import com.riders.thelab.feature.flightaware.ui.flight.PreviewProviderFlight
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun SearchFlightByRouteContent(flights: List<SegmentModel>, uiEvent: (UiEvent) -> Unit) {
    val lazyListState = rememberLazyListState()

    // this is to disable the ripple effect
    val interactionSource = remember { MutableInteractionSource() }

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
                LazyColumn(
                    modifier = Modifier
                        .size(width = this.maxWidth, height = this.maxHeight)
                        .indication(
                            indication = null,
                            interactionSource = interactionSource
                        ),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        /*FlightStatusCard(
                            flightId = NotBlankString.create(
                                flight.faFlightID.toString().split("-")[0]
                            ),
                            airlineOperatorId = flight.operatorID,
                            departureAirportIataCode = flight.origin?.codeIcao
                                ?: NotBlankString.create("N/A"),
                            arrivalAirportIataCode = flight.destination?.codeIcao
                                ?: NotBlankString.create("N/A"),
                            flightStatus = flight.status
                        )*/
                    }

                    item {
                        /*FlightInfoContainer(
                            departureDate = flight.estimatedOut ?: NotBlankString.create("N/A"),
                            departureTime = flight.scheduledOut ?: NotBlankString.create("N/A"),
                            arrivalDate = flight.estimatedOn ?: NotBlankString.create("N/A"),
                            arrivalTime = flight.scheduledOn ?: NotBlankString.create("N/A"),
                        )*/
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
private fun PreviewSearchFlightByRouteContent(@PreviewParameter(PreviewProviderFlight::class) flight: FlightModel) {
    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            /*SearchFlightByRouteContent(
                flights = flight,
                uiEvent = {}
            )*/
        }
    }
}