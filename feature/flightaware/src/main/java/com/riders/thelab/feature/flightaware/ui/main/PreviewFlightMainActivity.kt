package com.riders.thelab.feature.flightaware.ui.main

import SearchFlightContent
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.hideTooltip
import com.riders.thelab.core.ui.compose.utils.showTooltip
import com.riders.thelab.feature.flightaware.R
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.core.theme.buttonColor
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun FlightMainContent(
    hasConnection: Boolean,
    uiEvent: (UiEvent) -> Unit,
    searchPageIndex: Int,
    airportsNearBy: List<AirportModel>,
    isLoading: Boolean,
    departureExpanded: Boolean,
    onDepartureExpanded: (Boolean) -> Unit,
    departureSuggestions: List<AirportSearchModel>,
    arrivalExpanded: Boolean,
    onArrivalExpanded: (Boolean) -> Unit,
    arrivalSuggestions: List<AirportSearchModel>,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tooltipState = TooltipState()
    val lazyListState = rememberLazyListState()

    // this is to disable the ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    var onOutsideBoundariesClicked by remember { mutableStateOf(false) }

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    navigationIconColor = Color.White,
                    actions = {
                        AnimatedVisibility(visible = !hasConnection) {
                            TooltipBox(
                                tooltip = {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth(.85f)
                                            .shadow(
                                                elevation = 4.dp,
                                                shape = RoundedCornerShape(16.dp)
                                            ),
                                        elevation = CardDefaults.elevatedCardElevation(
                                            defaultElevation = 8.dp
                                        )
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(16.dp),
                                            text = "You are not connected to the internet. Please check your settings.",
                                            color = Color.Black
                                        )
                                    }
                                },
                                state = tooltipState,
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
                            ) {
                                IconButton(
                                    modifier = Modifier.pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = { showTooltip(scope, tooltipState) }
                                        )
                                    },
                                    onClick = {
                                        if (!tooltipState.isVisible) showTooltip(
                                            scope,
                                            tooltipState
                                        ) else hideTooltip(scope, tooltipState)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Error,
                                        contentDescription = null,
                                        tint = searchTextColor
                                    )
                                }
                            }
                        }
                    },
                    backgroundColor = backgroundColor
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
                        .fillMaxSize()
                        .indication(
                            indication = null,
                            interactionSource = interactionSource
                        ),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        SearchFlightContent(
                            uiEvent = uiEvent,
                            searchPageIndex = searchPageIndex,
                            onOutsideBoundariesClicked = onOutsideBoundariesClicked,
                            departureExpanded = departureExpanded,
                            onDepartureExpanded = onDepartureExpanded,
                            departureSuggestions = departureSuggestions,
                            arrivalExpanded = arrivalExpanded,
                            onArrivalExpanded = onArrivalExpanded,
                            arrivalSuggestions = arrivalSuggestions
                        )
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            onClick = {
                                scope.launch {
                                    onOutsideBoundariesClicked = true
                                    delay(250)
                                    onOutsideBoundariesClicked = false
                                }
                            },
                            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { (context as FlightMainActivity).launchAirportSearchActivity() },
                                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                                ) {
                                    Text(
                                        text = "See all Airports",
                                        style = TextStyle(color = Color.LightGray)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        AirportNearByContent(
                            hasInternetConnection = hasConnection,
                            uiEvent = uiEvent,
                            airports = airportsNearBy,
                            isLoading = isLoading
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
fun Footer() {
    TheLabTheme {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            ProvidedBy(
                providerIcon = R.drawable.ic_flightaware_logo,
                hasPadding = true,
                hasRoundedCorners = true,
                textColor = Color.White,
                backgroundColor = Color(0xFF002f5d)
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewFlightMainContent() {
    TheLabTheme {
        FlightMainContent(
            hasConnection = true,
            uiEvent = {},
            searchPageIndex = 0,
            airportsNearBy = emptyList(),
            isLoading = true,
            departureExpanded = true,
            onDepartureExpanded = {},
            departureSuggestions = listOf(),
            arrivalExpanded = true,
            onArrivalExpanded = {},
            arrivalSuggestions = listOf()
        )
    }
}


@DevicePreviews
@Composable
private fun PreviewFlightMainContentNoConnection() {
    TheLabTheme {
        FlightMainContent(
            hasConnection = false,
            uiEvent = {},
            searchPageIndex = 1,
            airportsNearBy = emptyList(),
            isLoading = false,
            departureExpanded = true,
            onDepartureExpanded = {},
            departureSuggestions = listOf(),
            arrivalExpanded = true,
            onArrivalExpanded = {},
            arrivalSuggestions = listOf()
        )
    }
}