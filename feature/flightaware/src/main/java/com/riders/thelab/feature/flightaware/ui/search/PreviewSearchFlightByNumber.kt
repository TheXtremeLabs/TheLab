package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.ui.flight.FlightDetailContent
import com.riders.thelab.feature.flightaware.ui.flight.FlightInfoContainer
import com.riders.thelab.feature.flightaware.ui.flight.FlightStatusCard
import com.riders.thelab.feature.flightaware.ui.main.Footer
import com.riders.thelab.feature.flightaware.utils.Constants
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
                            airlineOperatorId = flight.operatorID,
                            departureAirportIataCode = flight.origin?.codeIcao
                                ?: NotBlankString.create("N/A"),
                            arrivalAirportIataCode = flight.destination?.codeIcao
                                ?: NotBlankString.create("N/A"),
                            flightStatus = flight.status
                        )
                    }

                    item {
                        FlightInfoContainer(
                            departureDate = flight.estimatedOut ?: NotBlankString.create("N/A"),
                            departureTime = flight.scheduledOut ?: NotBlankString.create("N/A"),
                            arrivalDate = flight.estimatedOn ?: NotBlankString.create("N/A"),
                            arrivalTime = flight.scheduledOn ?: NotBlankString.create("N/A"),
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            FlightDetailContent(
                flight = flight,
                uiEvent = {}
            )
        }
    }
}