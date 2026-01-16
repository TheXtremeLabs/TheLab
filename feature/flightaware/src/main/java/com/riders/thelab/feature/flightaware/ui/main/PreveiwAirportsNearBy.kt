package com.riders.thelab.feature.flightaware.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.R
import com.riders.thelab.feature.flightaware.core.theme.buttonColor
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import com.riders.thelab.feature.flightaware.core.theme.textColor
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun AirportNearByItem(
    theme: AppTheme,
    darkTheme: Boolean,
    airport: AirportModel
) {
    val context = LocalContext.current

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_custom_min_height)),
            onClick = {
                airport.airportId?.let {
                    (context as FlightMainActivity).mFlightNavigator?.launchAirportSearchDetailActivity(it)
                } ?: run {
                    Timber.e("Recomposition | icaoCode is null")
                }
            },
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                /*GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 22.dp)
                        .zIndex(1f),
                    location = (airport.latitude!! to airport.longitude!!).toLocation(),
                    uiSettings = MapUiSettings(
                        compassEnabled = false,
                        myLocationButtonEnabled = false,
                        zoomGesturesEnabled = false,
                        zoomControlsEnabled = false,
                        scrollGesturesEnabled = false,
                        scrollGesturesEnabledDuringRotateOrZoom = false,
                        mapToolbarEnabled = false
                    ),
                    onMapLoaded = {
                        Timber.d("Map loaded")
                    }
                )
*/

                // Airport name, city and state
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1.5f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = airport.name.toString(),
                            color = textColor,
                            maxLines = 2
                        )
                        Text(
                            text = "${airport.city}, ${airport.state}",
                            color = textColor,
                            maxLines = 1
                        )
                    }

                    Row(
                        modifier = Modifier.weight(.5f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${airport.distance} km",
                            color = Color.LightGray,
                            maxLines = 1
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.LightGray
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // IATA CODE
                    if (null != airport.alternateId) {
                        Box(
                            modifier = Modifier.border(
                                width = 2.dp,
                                color = searchTextColor,
                                shape = RoundedCornerShape(4.dp)
                            ), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = airport.alternateId.toString(),
                                fontSize = 12.sp,
                                color = searchTextColor
                            )
                        }
                    }

                    // ICAO CODE
                    if (null != airport.airportId) {
                        Box(
                            modifier = Modifier.border(
                                width = 2.dp,
                                color = searchTextColor,
                                shape = RoundedCornerShape(4.dp)
                            ), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = airport.airportId.toString(),
                                fontSize = 12.sp,
                                color = searchTextColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AirportNearByContent(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier,
    hasInternetConnection: Boolean,
    airports: List<AirportModel>,
    uiEvent: (UiEvent) -> Unit,
    isLoading: Boolean
) {
    val configuration = LocalConfiguration.current

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    24.dp,
                    configuration.screenHeightDp.dp - dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_height)
                )
                .padding(horizontal = 16.dp)
                .then(modifier),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardBackgroundColor),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(id = R.string.placeholder_airports_near_by),
                    color = textColor
                )

                AnimatedContent(
                    modifier = Modifier.fillMaxWidth(),
                    targetState = airports.isNotEmpty(),
                    transitionSpec = {
                        fadeIn() + slideInHorizontally(
                            tween(
                                durationMillis = 200,
                                easing = LinearOutSlowInEasing
                            )
                        ) togetherWith fadeOut() + slideOutHorizontally(
                            tween(
                                durationMillis = 200,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    },
                    label = "AirportsAnimation"
                ) { targetState: Boolean ->
                    if (!targetState) {
                        Button(
                            onClick = { uiEvent.invoke(UiEvent.OnFetchAirportNearBy) },
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                            enabled = hasInternetConnection && !isLoading
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.placeholder_get_airports_near_by),
                                    color = textColor
                                )
                                AnimatedVisibility(visible = if (LocalInspectionMode.current) true else isLoading) {
                                    LabLoader(modifier = Modifier.size(30.dp))
                                }
                            }
                        }
                    } else {
                        val lazyListState = rememberLazyListState()

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            state = lazyListState,
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(items = airports.sortedBy { it.distance }) {
                                AirportNearByItem(
                                    theme = theme,
                                    darkTheme = darkTheme,
                                    airport = it
                                )
                            }
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
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewAirportNearByItem(@PreviewParameter(PreviewProviderAirport::class) airport: AirportModel) {
    TheLabTheme(theme = AppTheme.Default) {
        Box(
            modifier = Modifier.background(cardBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            AirportNearByItem(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(), airport)
        }
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewAirportNearByContentEmptyList(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Box(
            modifier = Modifier.background(cardBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            AirportNearByContent(
                theme = appTheme, darkTheme = isSystemInDarkTheme(),
                modifier = Modifier,
                hasInternetConnection = true,
                airports = emptyList(),
                uiEvent = {},
                isLoading = false
            )
        }
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewAirportNearByContent(@PreviewParameter(PreviewProviderAirport::class) airport: AirportModel) {
    TheLabTheme(theme = AppTheme.Default) {
        Box(
            modifier = Modifier.background(cardBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            AirportNearByContent(
                theme = AppTheme.Default,
                darkTheme = isSystemInDarkTheme(),
                modifier = Modifier,
                hasInternetConnection = true,
                airports = listOf(airport),
                uiEvent = {},
                isLoading = true
            )
        }
    }
}

