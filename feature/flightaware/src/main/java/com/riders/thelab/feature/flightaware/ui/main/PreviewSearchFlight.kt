import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.dropdown.LabDropdownMenu2
import com.riders.thelab.core.ui.compose.component.textfield.LabTextField2
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.R
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.core.theme.buttonColor
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import com.riders.thelab.feature.flightaware.core.theme.textColor
import com.riders.thelab.feature.flightaware.ui.airport.AirportSearchItem
import timber.log.Timber

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchFlightByCode(onUpdateFlightNumber: (String) -> Unit, onSearch: () -> Unit) {

    val textFieldState = rememberTextFieldState()
    val textFieldTextFlow by textFieldState.textAsFlow().collectAsState(initial = "")

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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.placeholder_search_a_flight),
                    style = TextStyle(color = Color.White)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), contentAlignment = Alignment.CenterStart
                ) {
                    LabTextField2(
                        modifier = Modifier.fillMaxSize(),
                        onOutsideBoundariesClicked = false,
                        textFieldState = textFieldState,
                        placeholder = stringResource(id = R.string.search_flight_by_number),
                        label = stringResource(id = R.string.placeholder_flight_number),
                        leadingContent = {
                            Icon(
                                modifier = Modifier.rotate(90f),
                                imageVector = Icons.Filled.AirplanemodeActive,
                                contentDescription = "icon_flight_takeoff",
                                tint = Color.LightGray
                            )
                        },
                        trailingContent = {
                            AnimatedContent(
                                targetState = textFieldState.text.isNotEmpty(),
                                label = "close icon transition"
                            ) { targetState ->
                                if (!targetState) {
                                    Box {}
                                } else {
                                    IconButton(onClick = { textFieldState.clearText() }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = "close icon"
                                        )
                                    }
                                }
                            }
                        },
                        focusedIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = backgroundColor,
                        unfocusedContainerColor = cardBackgroundColor,
                        hasBorders = true,
                        focusedBorderColor = searchTextColor,
                        unfocusedBorderColor = textColor
                    )
                }

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

    LaunchedEffect(textFieldTextFlow) {
        onUpdateFlightNumber(textFieldTextFlow.toString())
    }
}


@Composable
fun SearchFlightByDestination(
    onSearch: () -> Unit,
    onOutsideBoundariesClicked: Boolean,
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.placeholder_search_a_flight),
                    style = TextStyle(color = Color.White)
                )

                // Departure
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
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
                    LabDropdownMenu2(
                        modifier = Modifier.fillMaxWidth(),
                        query = departureQuery,
                        onUpdateQuery = onUpdateDepartureQuery,
                        onOutsideBoundariesClicked = onOutsideBoundariesClicked,
                        expanded = departureExpanded || departureSuggestions.isNotEmpty(),
                        onExpandedChanged = onDepartureExpanded,
                        placeholder = stringResource(id = R.string.search_flight_from),
                        label = stringResource(id = R.string.search_flight_from),
                        suggestions = departureSuggestions,
                        onOptionsSelected = onDepartureOptionsSelected,
                        dropdownItemContent = {
                            AirportSearchItem(item = departureSuggestions[it], isSuggestion = true)
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
                            if (departureQuery.trim().isBlank()) {
                                Timber.e("You cannot switch entries because the departure input is empty")
                                return@IconButton
                            }
                            if (arrivalQuery.trim().isBlank()) {
                                Timber.e("You cannot switch entries because the arrival input is empty")
                                return@IconButton
                            }

                            val temp = departureQuery
                            onUpdateDepartureQuery(arrivalQuery)
                            onUpdateArrivalQuery(temp)
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
                        .height(48.dp)
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
                    LabDropdownMenu2(
                        modifier = Modifier.fillMaxWidth(),
                        query = arrivalQuery,
                        onUpdateQuery = onUpdateArrivalQuery,
                        onOutsideBoundariesClicked = onOutsideBoundariesClicked,
                        expanded = arrivalExpanded || arrivalSuggestions.isNotEmpty(),
                        onExpandedChanged = onArrivalExpanded,
                        placeholder = stringResource(id = R.string.search_flight_destination),
                        label = stringResource(id = R.string.search_flight_destination),
                        suggestions = arrivalSuggestions,
                        onOptionsSelected = onArrivalOptionsSelected,
                        dropdownItemContent = {
                            AirportSearchItem(item = arrivalSuggestions[it], isSuggestion = true)
                        }
                    )
                }

                Button(
                    onClick = {
                        if (departureQuery.isEmpty() && arrivalQuery.isEmpty()) {
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            SearchFlightByCode({},{})
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewSearchFlightByDestination() {
    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            SearchFlightByDestination(
                onSearch = {},
                onOutsideBoundariesClicked = false,
                departureExpanded = false,
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
}