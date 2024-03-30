package com.riders.thelab.feature.flightaware.ui.main

import SearchFlightByCode
import SearchFlightByDestination
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabHorizontalViewPagerGeneric
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.tab.LabTabRow
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
    onSearchCategorySelected: (Int) -> Unit,
    flightNumberQuery: String,
    onUpdateFlightNumber: (String) -> Unit,
    onSearch: () -> Unit,
    departureExpanded: Boolean,
    onDepartureExpanded: (Boolean) -> Unit,
    departureQuery: String,
    onUpdateDepartureQuery: (String) -> Unit,
    departureSuggestions: List<AirportSearchModel>,
    onDepartureOptionsSelected: (AirportSearchModel) -> Unit,
    arrivalExpanded: Boolean,
    onArrivalExpanded: (Boolean) -> Unit,
    arrivalQuery: String,
    onUpdateArrivalQuery: (String) -> Unit,
    arrivalSuggestions: List<AirportSearchModel>,
    onArrivalOptionsSelected: (AirportSearchModel) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tooltipState = TooltipState()
    val lazyListState = rememberLazyListState()

    // this is to disable the ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    val tabs = listOf(
        stringResource(id = R.string.search_flight_by_id),
        stringResource(id = R.string.search_flight_by_route)
    )
    var tabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabs.size }

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
                                tooltip = { Text(text = "You are not connected to the internet. Please check your settings.") },
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
                                        tint = com.riders.thelab.core.ui.compose.theme.error
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
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource
                        ) {
                            scope.launch {
                                onOutsideBoundariesClicked = true
                                delay(250)
                                onOutsideBoundariesClicked = false
                            }
                        },
                    state = lazyListState,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .indication(
                                    indication = null,
                                    interactionSource = interactionSource
                                ),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                        ) {
                            LabTabRow(
                                modifier = Modifier.fillMaxWidth(),
                                items = tabs,
                                selectedItemIndex = tabIndex,
                                tabWidth = this@BoxWithConstraints.maxWidth / tabs.size,
                                selectedTextColor = searchTextColor,
                                unselectedTextColor = Color.Gray,
                                backgroundColor = backgroundColor,
                                indicatorColor = cardBackgroundColor,
                                hasCustomShape = true
                            ) { index ->
                                tabIndex = index
                                onSearchCategorySelected(tabIndex)
                            }

                            LabHorizontalViewPagerGeneric(
                                pagerState = pagerState,
                                items = tabs,
                                onCurrentPageChanged = {}
                            ) { page, _ ->
                                when (page) {
                                    0 -> SearchFlightByCode(
                                        flightNumberQuery = flightNumberQuery,
                                        onUpdateFlightNumber = onUpdateFlightNumber,
                                        onSearch = onSearch
                                    )

                                    1 -> SearchFlightByDestination(
                                        onSearch = onSearch,
                                        onOutsideBoundariesClicked = onOutsideBoundariesClicked,
                                        departureExpanded = departureExpanded,
                                        onDepartureExpanded = onDepartureExpanded,
                                        departureQuery = departureQuery,
                                        onUpdateDepartureQuery = onUpdateDepartureQuery,
                                        departureSuggestions = departureSuggestions,
                                        onDepartureOptionsSelected = onDepartureOptionsSelected,
                                        arrivalExpanded = arrivalExpanded,
                                        onArrivalExpanded = onArrivalExpanded,
                                        arrivalQuery = arrivalQuery,
                                        onUpdateArrivalQuery = onUpdateArrivalQuery,
                                        arrivalSuggestions = arrivalSuggestions,
                                        onArrivalOptionsSelected = onArrivalOptionsSelected
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
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
                        Footer()
                    }
                }
            }
        }
    }

    LaunchedEffect(tabIndex) {
        scope.launch { pagerState.animateScrollToPage(tabIndex) }
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
            onSearchCategorySelected = {},
            flightNumberQuery = "AFR4986",
            onUpdateFlightNumber = {},
            onSearch = {},
            departureExpanded = true,
            onDepartureExpanded = {},
            departureQuery = "None",
            onUpdateDepartureQuery = {},
            departureSuggestions = listOf(),
            onDepartureOptionsSelected = {},
            arrivalExpanded = true,
            onArrivalExpanded = {},
            arrivalQuery = "None",
            onUpdateArrivalQuery = {},
            arrivalSuggestions = listOf()
        ) {}
    }
}


@DevicePreviews
@Composable
private fun PreviewFlightMainContentNoConnection() {
    TheLabTheme {
        FlightMainContent(
            hasConnection = false,
            onSearchCategorySelected = {},
            flightNumberQuery = "AFR4986",
            onUpdateFlightNumber = {},
            onSearch = {},
            departureExpanded = true,
            onDepartureExpanded = {},
            departureQuery = "None",
            onUpdateDepartureQuery = {},
            departureSuggestions = listOf(),
            onDepartureOptionsSelected = {},
            arrivalExpanded = true,
            onArrivalExpanded = {},
            arrivalQuery = "None",
            onUpdateArrivalQuery = {},
            arrivalSuggestions = listOf()
        ) {}
    }
}