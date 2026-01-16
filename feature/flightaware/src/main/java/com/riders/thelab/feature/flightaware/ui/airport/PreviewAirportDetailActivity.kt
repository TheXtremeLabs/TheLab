package com.riders.thelab.feature.flightaware.ui.airport

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.LabBackButton
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.feature.flightaware.core.component.GoogleMap
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import com.riders.thelab.feature.flightaware.core.theme.textColor
import com.riders.thelab.feature.flightaware.data.local.model.compose.AirportDetailUiState
import com.riders.thelab.feature.flightaware.data.local.model.compose.ArrivalsUiState
import com.riders.thelab.feature.flightaware.data.local.model.compose.DeparturesUiState
import com.riders.thelab.feature.flightaware.data.local.model.toLocationModel
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
fun AirportDetailLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LabLoader(modifier = Modifier.size(72.dp))
    }
}

@Composable
fun AirportDetailSuccess(
    theme: AppTheme,
    darkTheme: Boolean,
    airport: AirportModel,
    departuresUiState: DeparturesUiState,
    arrivalsUiState: ArrivalsUiState,
    isFlightsFetched: Boolean,
    onFlightRequested: () -> Unit
) {
    val lazyListState = rememberLazyListState()
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
    var isLoadingVisible by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState
    ) {
        item {
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_width))
            ) {
                GoogleMap(
                    modifier = Modifier
                        .zIndex(2f)
                        .fillMaxSize(),
                    properties = properties,
                    uiSettings = uiSettings,
                    location = airport.toLocationModel(),
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
                    text = "${airport.name} (${airport.iataCode})",
                    style = Typography.displaySmall,
                    color = textColor
                )
                Text(
                    text = "${airport.city}",
                    style = Typography.bodyLarge,
                    color = Color.LightGray
                )
                if (!airport.state.isNullOrBlank()) {
                    Text(
                        text = "${airport.state}",
                        style = Typography.bodyMedium,
                        color = Color.LightGray

                    )
                }
                Text(
                    text = "${airport.timezone}",
                    style = Typography.bodyMedium,
                    color = Color.LightGray
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = {
                        isLoadingVisible = true
                        onFlightRequested()
                    },
                    enabled = !isFlightsFetched,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLoadingVisible) Color.LightGray.copy(
                            .5f
                        ) else searchTextColor
                    ),
                ) {
                    Text(
                        text = "See Flights for this airport",
                        color = textColor
                    )

                    AnimatedVisibility(visible = isLoadingVisible) {
                        LabLoader(modifier = Modifier.size(56.dp))
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = isFlightsFetched && departuresUiState is DeparturesUiState.Success && arrivalsUiState is ArrivalsUiState.Success,
            ) {
                DeparturesArrivals(
                    theme = theme,
                    darkTheme = darkTheme,
                    departureFlightsUiState = departuresUiState,
                    arrivalFlightsUiState = arrivalsUiState
                )
            }
        }
    }
}


@Composable
fun AirportDetailError(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message)
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AirportDetailContent(
    theme: AppTheme,
    darkTheme: Boolean,
    airportDetailUiState: AirportDetailUiState,
    departuresUiState: DeparturesUiState,
    arrivalsUiState: ArrivalsUiState,
    isFlightsFetched: Boolean,
    onFlightRequested: () -> Unit
) {

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(color = backgroundColor),
            /*topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    modifier = Modifier.zIndex(5f),
                    toolbarSize = ToolbarSize.SMALL,
                    withGradientBackground = false,
                    navigationIcon = {
                        LabBackButton(
                            theme = theme,
                            modifier = Modifier
                                .zIndex(5f)
                                .size(36.dp),
                            backgroundColor = cardBackgroundColor.copy(alpha = .5f)
                        )
                    },
                    actions = null
                )
            },*/
            containerColor = backgroundColor
        ) { _ ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                AnimatedContent(
                    modifier = Modifier
                        .zIndex(1f)
                        .fillMaxSize()
                        .background(color = backgroundColor),
                    targetState = airportDetailUiState,
                    transitionSpec = { fadeIn() + slideInHorizontally() togetherWith fadeOut() + slideOutHorizontally() },
                    label = "airport_detail_transition",
                    contentAlignment = Alignment.Center
                ) { targetState ->
                    when (targetState) {
                        is AirportDetailUiState.Loading -> AirportDetailLoading()
                        is AirportDetailUiState.Success -> AirportDetailSuccess(
                            theme = theme,
                            darkTheme = darkTheme,
                            airport = targetState.airport,
                            departuresUiState = departuresUiState,
                            arrivalsUiState = arrivalsUiState,
                            isFlightsFetched = isFlightsFetched,
                            onFlightRequested = onFlightRequested
                        )

                        is AirportDetailUiState.Error -> AirportDetailError(message = targetState.message)
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(24.dp)
                        .zIndex(10f),
                    contentAlignment = Alignment.Center
                ) {
                    LabBackButton(
                        theme = theme,
                        modifier = Modifier.zIndex(10f),
                        backgroundColor = cardBackgroundColor.copy(alpha = .85f),
                        iconTint = textColor
                    )
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
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewAirportDetailContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {

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

    TheLabTheme(theme = appTheme) {
        AirportDetailContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            airportDetailUiState = AirportDetailUiState.Success(airport),
            departuresUiState = DeparturesUiState.Success(listOf()),
            arrivalsUiState = ArrivalsUiState.Success(listOf()),
            isFlightsFetched = false
        ) {}
    }
}