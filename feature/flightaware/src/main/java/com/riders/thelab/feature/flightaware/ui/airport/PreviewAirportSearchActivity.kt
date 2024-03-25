package com.riders.thelab.feature.flightaware.ui.airport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportSearchContent() {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    var airportQuery: String by remember { mutableStateOf("") }
    val airports = listOf("CDG", "LAX", "ORY", "JFK", "PTP")
    val airportFoundCount = airports.size

    TheLabTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = airportQuery,
                    onValueChange = { newValue -> airportQuery = newValue },
                    placeholder = { Text(text = "Enter an airport ID (CDG, ORY, JFK,...)") },
                    label = { Text(text = "Airport ID") },
                    singleLine = true,
                    maxLines = 1
                )
            },
                navigationIcon = {
                    IconButton(onClick = { (context as AirportSearchActivity).backPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = "navigation_back_icon"
                        )
                    }
                })

        }) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                state = lazyListState,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "$airportFoundCount airport(s) found",
                        style = Typography.displaySmall
                    )
                }
                items(items = airports) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { (context as AirportSearchActivity).launchAirportDetail(item) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(3f)
                                    .padding(8.dp),
                                text = item
                            )

                            Box(
                                modifier = Modifier.weight(.5f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = null
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
@DevicePreviews
@Composable
private fun PreviewAirportSearchContent() {
    TheLabTheme {
        AirportSearchContent()
    }
}