package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.data.local.model.flight.SegmentModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import com.riders.thelab.feature.flightaware.core.theme.textColor
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun SearchFlightByRouteItem(flight: SegmentModel) {
    val locale = Locale.getDefault()
    val zoneId by remember { mutableStateOf(ZoneId.systemDefault()) }
    val departureLocalDateTime by remember {
        mutableStateOf(
            LocalDateTime.ofInstant(
                Instant.parse(flight.estimatedOut.toString()),
                zoneId
            )
        )
    }
    val arrivalLocalDateTime by remember {
        mutableStateOf(
            LocalDateTime.ofInstant(
                Instant.parse(flight.estimatedIn.toString()),
                zoneId
            )
        )
    }

    TheLabTheme {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Origin
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = flight.origin.codeIcao.toString(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = searchTextColor
                            )
                        )
                        Text(
                            text = flight.origin.city.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                color = textColor
                            )
                        )
                    }

                    // Date
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "${
                            ChronoUnit.HOURS.between(
                                departureLocalDateTime,
                                arrivalLocalDateTime
                            )
                        }h",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = textColor, textAlign = TextAlign.Center
                        ),
                        maxLines = 1
                    )

                    // Destination
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = flight.destination?.codeIcao.toString(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = searchTextColor
                            )
                        )

                        Text(
                            text = flight.destination?.city.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                color = textColor
                            )
                        )
                    }
                }

                // Departure and arrival estimated time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = departureLocalDateTime.toLocalTime().toString(),
                        color = textColor
                    )
                    Icon(
                        modifier = Modifier.rotate(90f),
                        imageVector = Icons.Filled.AirplanemodeActive,
                        contentDescription = "icon_flight_takeoff",
                        tint = Color.LightGray
                    )
                    Text(
                        text = arrivalLocalDateTime.toLocalTime().toString(),
                        color = textColor
                    )
                }

                // Airline and status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Flight number: ${flight.operatorID.toString()}\n${flight.operator.toString()}",
                        color = textColor
                    )

                    Text(
                        text = "Status: ${flight.status.toString()}",
                        color = textColor
                    )
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewSearchFlightByRouteItem(@PreviewParameter(PreviewProviderFlight::class) flight: FlightModel) {
    TheLabTheme {
        SearchFlightByRouteItem(flight.segments?.get(0)!!)
    }
}