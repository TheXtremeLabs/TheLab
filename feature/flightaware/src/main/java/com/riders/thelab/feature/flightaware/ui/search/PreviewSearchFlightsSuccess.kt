package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.core.theme.textColor
import com.riders.thelab.feature.flightaware.ui.main.Footer
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun SearchFlightsSuccessContent(
    currentDate: NotBlankString,
    flights: List<SearchFlightModel>,
    uiEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()

    // this is to disable the ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SearchFlightsHeader(currentDate = currentDate, flight = flights[0])

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = backgroundColor)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .size(width = this.maxWidth, height = this.maxHeight)
                        .padding(horizontal = 16.dp)
                        .indication(
                            indication = null,
                            interactionSource = interactionSource
                        ),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "Found ${flights.size} flight(s)",
                            color = textColor
                        )
                    }

                    itemsIndexed(items = flights) { _, item ->
                        SearchFlightItem(flight = item, uiEvent = uiEvent)
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
@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewSearchFlightsSuccess(@PreviewParameter(PreviewProviderFlights::class) flights: List<SearchFlightModel>) {
    TheLabTheme {
        SearchFlightsSuccessContent(
            currentDate = NotBlankString.create("24/04/2024"),
            flights = flights
        ) {}
    }
}