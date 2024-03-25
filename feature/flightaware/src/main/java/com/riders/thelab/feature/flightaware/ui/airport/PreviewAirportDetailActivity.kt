package com.riders.thelab.feature.flightaware.ui.airport

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.data.remote.dto.flight.Arrivals
import com.riders.thelab.core.data.remote.dto.flight.Departures
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.feature.flightaware.core.component.GoogleMap
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AirportDetailContent(
    airportModel: AirportModel,
    departureFlights: List<Departures>?,
    arrivalFlights: List<Arrivals>?,
    isFlightsFetched: Boolean,
    onFlightRequested: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val airportFlightsLazyListState = rememberLazyListState()

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                scrollGesturesEnabled = false,
                zoomControlsEnabled = false,
                zoomGesturesEnabled = false,
                myLocationButtonEnabled = false
            )
        )
    }
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(navigationIcon = { Icons.AutoMirrored.Filled.KeyboardArrowLeft })
            }
        ) { _ ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = lazyListState
            ) {

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.max_card_image_width))
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            properties = properties,
                            uiSettings = uiSettings,
                            location = (airportModel.latitude!! to airportModel.longitude!!).toLocation(),
                            onMapLoaded = { Timber.d("Map loaded") }
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "${airportModel.name} (${airportModel.iataCode})",
                            style = Typography.displaySmall
                        )
                        Text(
                            text = "${airportModel.city}",
                            style = Typography.bodyLarge
                        )
                        if (!airportModel.state.isNullOrBlank()) {
                            Text(
                                text = "${airportModel.state}",
                                style = Typography.bodyMedium
                            )
                        }
                        Text(
                            text = "${airportModel.timezone}",
                            style = Typography.bodyMedium
                        )
                    }
                }

                item {
                    Button(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = onFlightRequested,
                        enabled = !isFlightsFetched
                    ) {
                        Text(text = "See Flights for this airport")
                    }
                }

                item {
                    AnimatedVisibility(visible = isFlightsFetched && !departureFlights.isNullOrEmpty() && !arrivalFlights.isNullOrEmpty()) {
                        DeparturesArrivals(
                            departureFlights = departureFlights!!,
                            arrivalFlights = arrivalFlights!!
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
private fun PreviewAirportDetailContent() {

    val airport = AirportModel(
        airportId = "LFPG",
        alternateId = "CDG",
        icaoCode = "LFPG",
        iataCode = "CDG",
        lidCode = null,
        name = "Paris-Charles-de-Gaulle",
        type = "Airport",
        elevation = "392",
        city = "Paris",
        state = null,
        longitude = 2.55,
        latitude = 49.012779,
        timezone = "Europe/Paris",
        wikiUrl = "https://en.wikipedia.org/wiki/Charles_de_Gaulle_Airport",
        airportFlightUrl = "/airports/LFPG/flights"
    )
    TheLabTheme {
        AirportDetailContent(airport, null, null, false, {})
    }
}