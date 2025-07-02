package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.ui.flight.FlightInfoContainer
import com.riders.thelab.feature.flightaware.ui.flight.FlightStatusCard
import com.riders.thelab.feature.flightaware.ui.main.Footer
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun SearchFlightByNumberContent(
    theme: AppTheme,
    darkTheme: Boolean,
    flight: FlightModel,
    uiEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    // this is to disable the ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
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
                            theme = theme,
                            darkTheme = darkTheme,
                            flightId =
                                flight.faFlightID.toString().split("-")[0].toNotBlankString()
                                    .getOrThrow(),
                            airlineIATA = flight.identIATA ?: flight.identICAO!!,
                            departureAirportIataCode = flight.origin?.codeIcao
                                ?: "N/A".toNotBlankString().getOrThrow(),
                            arrivalAirportIataCode = flight.destination?.codeIcao
                                ?: "N/A".toNotBlankString().getOrThrow(),
                            flightStatus = flight.status
                        )
                    }

                    item {
                        FlightInfoContainer(
                            theme = theme,
                            darkTheme = darkTheme,
                            airline = flight.operatorID,
                            aircraftType = flight.aircraftType ?: "N/A".toNotBlankString()
                                .getOrThrow(),
                            estimatedDepartureDate = flight.estimatedOut
                                ?: "N/A".toNotBlankString().getOrThrow(),
                            estimatedDepartureTime = flight.estimatedOut
                                ?: "N/A".toNotBlankString().getOrThrow(),
                            estimatedArrivalDate = flight.estimatedIn
                                ?: "N/A".toNotBlankString().getOrThrow(),
                            estimatedArrivalTime = flight.estimatedIn
                                ?: "N/A".toNotBlankString().getOrThrow(),
                            actualDepartureDate = flight.actualOut ?: "N/A".toNotBlankString()
                                .getOrThrow(),
                            actualDepartureTime = flight.actualOut ?: "N/A".toNotBlankString()
                                .getOrThrow(),
                            actualArrivalDate = flight.actualIn ?: "N/A".toNotBlankString()
                                .getOrThrow(),
                            actualArrivalTime = flight.actualIn ?: "N/A".toNotBlankString()
                                .getOrThrow(),
                        )
                    }

                    item {
                        Footer(theme = theme)
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
    TheLabTheme(theme = AppTheme.Default) {
        SearchFlightByNumberContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            flight = flight
        ) {
        }
    }
}