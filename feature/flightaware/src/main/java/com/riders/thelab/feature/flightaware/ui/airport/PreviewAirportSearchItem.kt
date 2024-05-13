package com.riders.thelab.feature.flightaware.ui.airport

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.searchTextColor


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun AirportSearchItemForSearchScreen(item: AirportSearchModel) {
    val context = LocalContext.current

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                item.icaoCode?.let {
                    (context as AirportSearchActivity).launchAirportDetail(it.toString())
                }
            },
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier.weight(4f),
                        text = item.name.toString(),
                        color = Color.White,
                        maxLines = 2
                    )

                    Box(modifier = Modifier.weight(.5f), contentAlignment = Alignment.CenterEnd) {
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
                    if (null != item.iataCode) {
                        Box(
                            modifier = Modifier.border(
                                width = 2.dp,
                                color = searchTextColor,
                                shape = RoundedCornerShape(4.dp)
                            ), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = item.iataCode.toString(),
                                fontSize = 14.sp,
                                color = searchTextColor
                            )
                        }
                    }

                    // ICAO CODE
                    if (null != item.icaoCode) {
                        Box(
                            modifier = Modifier.border(
                                width = 2.dp,
                                color = searchTextColor,
                                shape = RoundedCornerShape(4.dp)
                            ), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = item.icaoCode.toString(),
                                fontSize = 14.sp,
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
fun AirportSearchItemForSuggestion(item: AirportSearchModel) {
    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(0.dp)),
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
            shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.weight(3f),
                    text = item.name.toString(),
                    color = Color.White,
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // IATA CODE
                    if (null != item.iataCode) {
                        Box(
                            modifier = Modifier.border(
                                width = 2.dp,
                                color = searchTextColor,
                                shape = RoundedCornerShape(4.dp)
                            ), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = item.iataCode.toString(),
                                fontSize = 14.sp,
                                color = searchTextColor
                            )
                        }
                    }

                    // ICAO CODE
                    if (null != item.icaoCode) {
                        Box(
                            modifier = Modifier.border(
                                width = 2.dp,
                                color = searchTextColor,
                                shape = RoundedCornerShape(4.dp)
                            ), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = item.icaoCode.toString(),
                                fontSize = 14.sp,
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
fun AirportSearchItem(item: AirportSearchModel, isSuggestion: Boolean) {
    if (!isSuggestion) {
        AirportSearchItemForSearchScreen(item = item)
    } else {
        AirportSearchItemForSuggestion(item = item)
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewAirportSearchItemForSearchScreen(@PreviewParameter(PreviewProviderAirportSearch::class) item: AirportSearchModel) {
    TheLabTheme {
        AirportSearchItemForSearchScreen(item)
    }
}

@DevicePreviews
@Composable
private fun PreviewAirportSearchItemForSuggestion(@PreviewParameter(PreviewProviderAirportSearch::class) item: AirportSearchModel) {
    TheLabTheme {
        AirportSearchItemForSuggestion(item)
    }
}