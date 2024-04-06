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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.maps.android.compose.MapUiSettings
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.R
import com.riders.thelab.feature.flightaware.core.component.GoogleMap
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
fun AirportNearByItem(airport: AirportModel) {
    val context = LocalContext.current

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_mid_height)),
            onClick = {
                airport.icaoCode?.let {
                    (context as FlightMainActivity).launchAirportDetail(it.toString())
                } ?: run {
                    Timber.e("icaoCode is null")
                }
            },
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                GoogleMap(
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.4f)
                        .background(color = cardBackgroundColor)
                        .padding(8.dp)
                        .zIndex(5f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(4f),
                            text = airport.name.toString(),
                            color = Color.White,
                            maxLines = 2
                        )

                        Box(
                            modifier = Modifier.weight(.5f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // IATA CODE
                        if (null != airport.iataCode) {
                            Box(
                                modifier = Modifier.border(
                                    width = 2.dp,
                                    color = searchTextColor,
                                    shape = RoundedCornerShape(4.dp)
                                ), contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = airport.iataCode.toString(),
                                    fontSize = 12.sp,
                                    color = searchTextColor
                                )
                            }
                        }

                        // ICAO CODE
                        if (null != airport.icaoCode) {
                            Box(
                                modifier = Modifier.border(
                                    width = 2.dp,
                                    color = searchTextColor,
                                    shape = RoundedCornerShape(4.dp)
                                ), contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = airport.icaoCode.toString(),
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
}

@Composable
fun AirportNearByContent(
    hasInternetConnection: Boolean,
    airports: List<AirportModel>,
    uiEvent: (UiEvent) -> Unit,
    isLoading: Boolean
) {
    val configuration = LocalConfiguration.current

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    24.dp,
                    configuration.screenHeightDp.dp - dimensionResource(id = com.riders.thelab.core.ui.R.dimen.max_card_image_height)
                )
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
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
                                AnimatedVisibility(visible = isLoading) {
                                    LabLoader(modifier = Modifier.size(30.dp))
                                }
                            }
                        }
                    } else {
                        val lazyListState = rememberLazyListState()

                        LazyColumn(modifier = Modifier.fillMaxWidth(), state = lazyListState) {
                            items(items = airports) {
                                AirportNearByItem(airport = it)
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
@DevicePreviews
@Composable
private fun PreviewAirportNearByItem(@PreviewParameter(PreviewProviderAirport::class) airport: AirportModel) {
    TheLabTheme {
        Box(
            modifier = Modifier.background(cardBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            AirportNearByItem(airport)
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewAirportNearByContentEmptyList() {
    TheLabTheme {
        Box(
            modifier = Modifier.background(cardBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            AirportNearByContent(
                hasInternetConnection = true,
                airports = emptyList(),
                uiEvent = {},
                isLoading = false
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewAirportNearByContent(@PreviewParameter(PreviewProviderAirport::class) airport: AirportModel) {
    TheLabTheme {
        Box(
            modifier = Modifier.background(cardBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            AirportNearByContent(
                hasInternetConnection = true,
                airports = listOf(airport),
                uiEvent = {},
                isLoading = true
            )
        }
    }
}

