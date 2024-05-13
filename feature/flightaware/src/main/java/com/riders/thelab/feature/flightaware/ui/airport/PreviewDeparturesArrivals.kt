package com.riders.thelab.feature.flightaware.ui.airport

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.remote.dto.flight.Arrivals
import com.riders.thelab.core.data.remote.dto.flight.Departures
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import com.riders.thelab.feature.flightaware.core.theme.textColor
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////

@Composable
fun FlightHeader(title: String, headerItems: List<String>) {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = cardBackgroundColor),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = title, style = Typography.headlineSmall,
                color = Color.LightGray
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(headerItems.size) {
                    Text(
                        modifier = Modifier.weight(if (2 == it) .25f else 1f),
                        text = headerItems[it],
                        color = Color.LightGray
                    )
                }
            }

            HorizontalDivider(color = searchTextColor)
        }
    }
}


@Composable
fun DeparturesArrivalsItem(
    flightId: NotBlankString,
    departureAirportId: NotBlankString,
    departureAirportName: NotBlankString,
    departureGate: NotBlankString,
    departureTerminal: NotBlankString,
    arrivalAirportId: NotBlankString,
    arrivalAirportName: NotBlankString,
    arrivalGate: NotBlankString,
    arrivalTerminal: NotBlankString
) {
    TheLabTheme {
        Row(modifier = Modifier.fillMaxWidth()) {

            Text(modifier = Modifier.weight(1f), text = flightId.toString(), color = textColor)

            Column(modifier = Modifier.weight(1f)) {
                Text(text = departureAirportId.toString(), color = textColor)
                Row {
                    Text(text = departureTerminal.toString(), color = textColor)
                    Text(text = departureGate.toString(), color = textColor)
                }
            }

            Text(modifier = Modifier.weight(.4f), text = " - ", color = textColor)

            Column(modifier = Modifier.weight(1f)) {
                Text(text = arrivalAirportId.toString(), color = textColor)
                Row {
                    Text(text = arrivalTerminal.toString(), color = textColor)
                    Text(text = arrivalGate.toString(), color = textColor)
                }
            }
        }
    }
}


@OptIn(ExperimentalKotoolsTypesApi::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DeparturesArrivals(departureFlights: List<Departures>, arrivalFlights: List<Arrivals>) {
    val airportFlightsLazyListState = rememberLazyListState()

    val departuresArrivalsHeader = listOf("Flight ID", "Departure", " ", "Arrival")
    val naValue: NotBlankString = NotBlankString.create("N/A")

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.max_card_image_width)),
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
        ) {
            BoxWithConstraints {
                LazyColumn(
                    modifier = Modifier
                        .width(this.maxWidth)
                        .height(this.maxHeight)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    state = airportFlightsLazyListState
                ) {
                    stickyHeader {
                        FlightHeader(title = "Departures", headerItems = departuresArrivalsHeader)
                    }

                    items(items = departureFlights) {
                        DeparturesArrivalsItem(
                            flightId = it.flightNumber ?: naValue,
                            departureAirportId = it.identIATA ?: naValue,
                            departureAirportName = it.identIATA ?: naValue,
                            departureGate = it.gateOrigin ?: naValue,
                            departureTerminal = it.terminalOrigin ?: naValue,
                            arrivalAirportId = it.operatorICAO ?: naValue,
                            arrivalAirportName = it.operatorICAO ?: naValue,
                            arrivalGate = it.gateDestination ?: naValue,
                            arrivalTerminal = it.terminalDestination ?: naValue
                        )
                    }

                    stickyHeader {
                        FlightHeader(title = "Arrivals", headerItems = departuresArrivalsHeader)
                    }

                    items(items = arrivalFlights) {
                        DeparturesArrivalsItem(
                            flightId = it.flightNumber ?: naValue,
                            departureAirportId = it.identIATA ?: naValue,
                            departureAirportName = it.identIATA ?: naValue,
                            departureGate = it.gateOrigin ?: naValue,
                            departureTerminal = it.terminalOrigin ?: naValue,
                            arrivalAirportId = it.operatorICAO ?: naValue,
                            arrivalAirportName = it.operatorICAO ?: naValue,
                            arrivalGate = it.gateDestination ?: naValue,
                            arrivalTerminal = it.terminalDestination ?: naValue
                        )
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
private fun PreviewFlightHeader() {
    TheLabTheme {
        FlightHeader("Departures", listOf("Flight ID", "Departure", " ", "Arrival"))
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewDeparturesArrivalsItem() {
    TheLabTheme {
        Box(modifier = Modifier.background(color = cardBackgroundColor)) {
            DeparturesArrivalsItem(
                flightId = NotBlankString.create("EZ1515FR"),
                departureAirportId = NotBlankString.create("CDG"),
                departureAirportName = NotBlankString.create("Paris-Charles-de-Gaulle"),
                departureGate = NotBlankString.create("2"),
                departureTerminal = NotBlankString.create("D"),
                arrivalAirportId = NotBlankString.create("LAX"),
                arrivalAirportName = NotBlankString.create("Int'l Los Angeles"),
                arrivalGate = NotBlankString.create("5"),
                arrivalTerminal = NotBlankString.create("D")
            )
        }
    }
}


@DevicePreviews
@Composable
private fun PreviewDeparturesArrivals() {
    TheLabTheme {
        DeparturesArrivals(emptyList(), emptyList())
    }
}