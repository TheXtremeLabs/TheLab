package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.ui.flight.FlightInfoContainer
import com.riders.thelab.feature.flightaware.ui.flight.FlightStatusCard
import com.riders.thelab.feature.flightaware.ui.main.Footer
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun SearchFlightByNumberContent(flight: FlightModel, uiEvent: (UiEvent) -> Unit) {
    val lazyListState = rememberLazyListState()
    // this is to disable the ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    toolbarSize = ToolbarSize.SMALL,
                    title = "Flight Details",
                    mainCustomContent = null,
                    toolbarMaxHeight = 56.dp,
                    navigationIconColor = Color.White,
                    backgroundColor = backgroundColor,
                    withGradientBackground = false
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
                        .padding(top = 16.dp)
                        .indication(
                            indication = null,
                            interactionSource = interactionSource
                        ),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        FlightStatusCard(
                            flightId = NotBlankString.create(
                                flight.faFlightID.toString().split("-")[0]
                            ),
                            airlineIATA = flight.identIATA ?: flight.identICAO!!,
                            departureAirportIataCode = flight.origin?.codeIcao
                                ?: NotBlankString.create("N/A"),
                            arrivalAirportIataCode = flight.destination?.codeIcao
                                ?: NotBlankString.create("N/A"),
                            flightStatus = flight.status
                        )
                    }

                    item {
                        FlightInfoContainer(
                            airline = flight.operatorID,
                            aircraftType = flight.aircraftType ?: NotBlankString.create("N/A"),
                            estimatedDepartureDate = flight.estimatedOut
                                ?: NotBlankString.create("N/A"),
                            estimatedDepartureTime = flight.estimatedOut
                                ?: NotBlankString.create("N/A"),
                            estimatedArrivalDate = flight.estimatedIn
                                ?: NotBlankString.create("N/A"),
                            estimatedArrivalTime = flight.estimatedIn
                                ?: NotBlankString.create("N/A"),
                            actualDepartureDate = flight.actualOut ?: NotBlankString.create("N/A"),
                            actualDepartureTime = flight.actualOut ?: NotBlankString.create("N/A"),
                            actualArrivalDate = flight.actualIn ?: NotBlankString.create("N/A"),
                            actualArrivalTime = flight.actualIn ?: NotBlankString.create("N/A"),
                        )
                    }

                    item {
                        Footer()
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
private fun PreviewSearchFlightByNumberContent(@PreviewParameter(PreviewProviderFlight::class) flight: FlightModel) {
    TheLabTheme {
        SearchFlightByNumberContent(flight = flight) {
        }
    }
}