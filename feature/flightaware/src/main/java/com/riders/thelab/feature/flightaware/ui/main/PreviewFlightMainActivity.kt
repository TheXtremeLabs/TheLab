package com.riders.thelab.feature.flightaware.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabHorizontalViewPagerGeneric
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.tab.LabTabRow
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.launch


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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Search a flight",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    BasicTextField2(
                        modifier = Modifier.fillMaxWidth(),
                        state = textFieldState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        decorator = { innerTextField ->
                            if (textFieldState.text.isEmpty())
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
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

                            // you have to invoke this function then cursor will focus and you will able to write something
                            innerTextField.invoke()
                        }
                    )

                    Button(onClick = {
                        if (textFieldState.text.isEmpty()) {
                            return@Button
                        }
                        onSearch()
                    }) {
                        Text("Search")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchFlightByDestination(onSearch: () -> Unit) {

    val departureTextFieldState = rememberTextFieldState()
    val arrivalTextFieldState = rememberTextFieldState()

    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(modifier = Modifier.fillMaxWidth(), text = "Search a flight")

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
                            contentDescription = "icoon_flight_takeoff"
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
                            contentDescription = "icon_flight_land"
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
                        }
                    ) {
                        Text("Search")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun FlightMainContent(
    airportsSize: Int,
    onSearchCategorySelected: (Int) -> Unit,
    onSearch: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val tabs = listOf("Search Flight by ID", "Search Flight By Destination")
    var tabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabs.size }

    TheLabTheme {
        Scaffold(topBar = { TheLabTopAppBar() }) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    item {
                        LabTabRow(
                            modifier = Modifier.fillMaxWidth(),
                            items = tabs,
                            selectedItemIndex = tabIndex,
                            tabWidth = this@BoxWithConstraints.maxWidth / 2
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
                                1 -> SearchFlightByDestination(onSearch = onSearch)
                            }
                        }
                    }


                    item {

                        Box(
                            modifier = Modifier.fillMaxWidth(.75f),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(modifier = Modifier, shape = CircleShape) {
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .wrapContentSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Airports size: $airportsSize")
                                }
                            }
                        }
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
        SearchFlightByCode() {}
    }
}

@DevicePreviews
@Composable
private fun PreviewSearchFlightByDestination() {
    TheLabTheme {
        SearchFlightByDestination {
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewFlightMainContent() {
    TheLabTheme {
        FlightMainContent(5, {}, {})
    }
}