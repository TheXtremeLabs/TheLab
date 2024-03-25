package com.riders.thelab.feature.flightaware.ui.airport

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.remote.dto.flight.Arrivals
import com.riders.thelab.core.data.remote.dto.flight.Departures
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
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

            Text(modifier = Modifier.weight(1f), text = flightId.toString())

            Column(modifier = Modifier.weight(1f)) {
                Text(text = departureAirportId.toString())
                Row {
                    Text(text = departureTerminal.toString())
                    Text(text = departureGate.toString())
                }
            }

            Text(modifier = Modifier.weight(1f), text = " - ")


            Column(modifier = Modifier.weight(1f)) {
                Text(text = arrivalAirportId.toString())
                Row {
                    Text(text = arrivalTerminal.toString())
                    Text(text = arrivalGate.toString())
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

    val departuresArrivalsHeader = listOf("Flight ID", "Departure", "", "Arrival")
    val naValue: NotBlankString = NotBlankString.create("N/A")

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.max_card_image_width))
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

                    item {
                        Text(text = "Departures", style = Typography.headlineSmall)
                    }

                    stickyHeader {
                        Card {


                        Row(modifier = Modifier.fillMaxWidth()) {
                            repeat(departuresArrivalsHeader.size) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = departuresArrivalsHeader[it]
                                )
                            }
                        }
                    }
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

                    item {
                        Text(text = "Arrivals", style = Typography.headlineSmall)
                    }

                    stickyHeader {
                        Card {


                        Row(modifier = Modifier.fillMaxWidth()) {
                            repeat(departuresArrivalsHeader.size) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = departuresArrivalsHeader[it]
                                )
                            }
                        }}
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
@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewDeparturesArrivalsItem() {
    TheLabTheme {
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

@DevicePreviews
@Composable
private fun PreviewDeparturesArrivals() {
    TheLabTheme {
        DeparturesArrivals(emptyList(), emptyList())
    }
}