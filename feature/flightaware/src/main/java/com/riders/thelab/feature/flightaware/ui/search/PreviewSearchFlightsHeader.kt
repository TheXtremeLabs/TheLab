package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.component.DottedLink
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor
import com.riders.thelab.feature.flightaware.core.theme.textColor
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun SearchFlightsHeader(currentDate: NotBlankString, flight: SearchFlightModel) {
    TheLabTheme {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardBackgroundColor),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.max_card_image_height)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(.5f),
                    text = "Search Flight(s)",
                    color = textColor
                )

                // Top Flight Icon
                DottedLink(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(.85f)
                        .zIndex(5f)
                )

                // Bottom flight route info
                Row(
                    modifier = Modifier
                        .weight(.75f)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 32.dp),
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
                            text = flight.origin?.city.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                color = textColor
                            )
                        )
                        Text(
                            text = flight.origin?.codeIcao.toString(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = searchTextColor
                            )
                        )
                    }

                    // Date
                    Text(
                        modifier = Modifier.weight(1f),
                        text = currentDate.toString(),
                        style = TextStyle(fontSize = 14.sp),
                        maxLines = 1,
                        color = textColor
                    )

                    // Destination
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = flight.destination?.city.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                color = textColor
                            )
                        )
                        Text(
                            text = flight.destination?.codeIcao.toString(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = searchTextColor
                            )
                        )
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
@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewSearchFlightsHeader(@PreviewParameter(PreviewProviderFlight::class) flight: SearchFlightModel) {
    TheLabTheme {
        SearchFlightsHeader(currentDate = NotBlankString.create("24/04/2024"), flight = flight)
    }
}