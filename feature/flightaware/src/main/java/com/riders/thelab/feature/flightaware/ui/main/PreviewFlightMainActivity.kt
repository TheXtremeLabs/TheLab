package com.riders.thelab.feature.flightaware.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabHorizontalViewPagerGeneric
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.tab.LabTabRow
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.R
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.core.theme.buttonColor
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import kotlinx.coroutines.launch
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchFlightByCode(onSearch: () -> Unit) {

    val textFieldState = rememberTextFieldState()

    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Search a flight",
                    style = TextStyle(color = Color.White)
                )

                BasicTextField2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    state = textFieldState,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    decorator = { innerTextField ->
                        if (textFieldState.text.trim().isEmpty()) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Enter Flight code",
                                style = TextStyle(
                                    fontWeight = FontWeight.W400,
                                    color = if (!isSystemInDarkTheme()) Color.DarkGray.copy(
                                        alpha = .56f
                                    ) else Color.LightGray.copy(
                                        alpha = .56f
                                    )
                                )
                            )
                        } else {
                            Box(
                                modifier = Modifier.size(30.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                IconButton(onClick = { textFieldState.clearText() }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                        // you have to invoke this function then cursor will focus and you will able to write something
                        innerTextField.invoke()
                    }
                )

                Button(
                    onClick = {
                        if (textFieldState.text.isEmpty()) {
                            return@Button
                        }
                        onSearch()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(text = "Search", style = TextStyle(color = Color.LightGray))
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchFlightByDestination(
    departureAirport: (String) -> Unit,
    arrivalAirport: (String) -> Unit,
    onSearch: () -> Unit
) {

    val departureTextFieldState = rememberTextFieldState()
    val arrivalTextFieldState = rememberTextFieldState()

    val departureAirportText by departureTextFieldState.textAsFlow().collectAsState(initial = "")
    val arrivalAirportText by departureTextFieldState.textAsFlow().collectAsState(initial = "")

    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Search a flight",
                    style = TextStyle(color = Color.White)
                )

                // Departure
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.FlightTakeoff,
                        contentDescription = "icon_flight_takeoff",
                        tint = Color.LightGray
                    )

                    // Departure
                    BasicTextField2(
                        modifier = Modifier.fillMaxWidth(),
                        state = departureTextFieldState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        decorator = { innerTextField ->
                            if (departureTextFieldState.text.isEmpty())
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    text = "Enter Departure Airport",
                                    style = TextStyle(
                                        fontWeight = FontWeight.W400,
                                        color = if (!isSystemInDarkTheme()) Color.DarkGray.copy(
                                            alpha = .56f
                                        ) else Color.LightGray.copy(
                                            alpha = .56f
                                        )
                                    )
                                )

                            // you have to invoke this function then cursor will focus and you will able to write something
                            innerTextField.invoke()
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider()

                    IconButton(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface),
                        onClick = {
                            if (departureTextFieldState.text.trim()
                                    .isNotBlank() && arrivalTextFieldState.text.trim().isNotBlank()
                            ) {
                                val temp = departureTextFieldState.text.toString()
                                departureTextFieldState.setTextAndPlaceCursorAtEnd(
                                    arrivalTextFieldState.text.toString()
                                )
                                arrivalTextFieldState.setTextAndPlaceCursorAtEnd(
                                    temp
                                )
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.rotate(90f),
                            imageVector = Icons.Filled.SyncAlt,
                            contentDescription = null,
                            tint = if (!isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                        )
                    }
                }


                // Arrival
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.FlightLand,
                        contentDescription = "icon_flight_land",
                        tint = Color.LightGray
                    )

                    // Arrival
                    BasicTextField2(
                        modifier = Modifier.fillMaxWidth(),
                        state = arrivalTextFieldState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        decorator = { innerTextField ->
                            if (arrivalTextFieldState.text.isEmpty())
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    text = "Enter Arrival Airport",
                                    style = TextStyle(
                                        fontWeight = FontWeight.W400,
                                        color = if (!isSystemInDarkTheme()) Color.DarkGray.copy(
                                            alpha = .56f
                                        ) else Color.LightGray.copy(
                                            alpha = .56f
                                        )
                                    )
                                )

                            // you have to invoke this function then cursor will focus and you will able to write something
                            innerTextField.invoke()
                        }
                    )
                }

                Button(
                    onClick = {
                        if (departureTextFieldState.text.isEmpty() && arrivalTextFieldState.text.isEmpty()) {
                            return@Button
                        }
                        onSearch()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(
                        text = "Search",
                        style = TextStyle(color = Color.LightGray)
                    )
                }
            }
        }
    }

    LaunchedEffect(departureAirportText) {
        Timber.d("LaunchedEffect | departureAirportText: ${departureAirportText.toString()} | coroutineContext: ${this.coroutineContext}")
        departureAirport(departureAirportText.toString())
    }

    LaunchedEffect(arrivalAirportText.toString()) {
        Timber.d("LaunchedEffect | arrivalAirportText: ${arrivalAirportText.toString()} | coroutineContext: ${this.coroutineContext}")
        arrivalAirport(arrivalAirportText.toString())
    }
}

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


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun FlightMainContent(
    airportsSize: Int,
    onSearchCategorySelected: (Int) -> Unit,
    departureAirport: (String) -> Unit,
    arrivalAirport: (String) -> Unit,
    onSearch: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    // this is to disable the ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    val tabs = listOf(
        stringResource(id = R.string.search_flight_by_id),
        stringResource(id = R.string.search_flight_by_route)
    )
    var tabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabs.size }

    TheLabTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor),
            topBar = {
                TheLabTopAppBar(
                    navigationIconColor = Color.LightGray,
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
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                        ) {
                            LabTabRow(
                                modifier = Modifier.fillMaxWidth(),
                                items = tabs,
                                selectedItemIndex = tabIndex,
                                tabWidth = this@BoxWithConstraints.maxWidth / tabs.size,
                                selectedTextColor = searchTextColor,
                                unselectedTextColor = Color.Gray,
                                backgroundColor = Color.Transparent
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
                                    0 -> SearchFlightByCode(onSearch = onSearch)
                                    1 -> SearchFlightByDestination(
                                        departureAirport = departureAirport,
                                        arrivalAirport = arrivalAirport,
                                        onSearch = onSearch
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

                                Text(
                                    text = "Airports size: $airportsSize",
                                    style = TextStyle(color = Color.White)
                                )

                                Button(
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
private fun PreviewSearchFlightByCode() {
    TheLabTheme {
        SearchFlightByCode {}
    }
}

@DevicePreviews
@Composable
private fun PreviewSearchFlightByDestination() {
    TheLabTheme {
        SearchFlightByDestination({ }, { }, {})
    }
}

@DevicePreviews
@Composable
private fun PreviewFlightMainContent() {
    TheLabTheme {
        FlightMainContent(10, {}, {}, {}, {})
    }
}