package com.riders.thelab.feature.flightaware.ui.flight

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.feature.flightaware.core.component.DottedLink
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor
import com.riders.thelab.feature.flightaware.core.theme.textColor
import com.riders.thelab.feature.flightaware.ui.search.SearchFlightActivity
import com.riders.thelab.feature.flightaware.utils.Constants
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val zoneId: ZoneId = ZoneId.systemDefault()
private fun NotBlankString.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.parse(this.toString()), zoneId)


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun FlightStatusCard(
    flightId: NotBlankString,
    airlineIATA: NotBlankString,
    departureAirportIataCode: NotBlankString,
    arrivalAirportIataCode: NotBlankString,
    flightStatus: NotBlankString
) {
    val context = LocalContext.current

    val flightIATA = airlineIATA.toString().take(2)
    val painter = getCoilAsyncImagePainter(
        context = context,
        dataUrl = "${Constants.ENDPOINT_FLIGHT_FULL_LOGO}$flightIATA${Constants.EXTENSION_SVG}",
        isSvg = true,
        placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors
    )

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                // Flight ID & Operator ID
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = flightId.toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.W200,
                            fontSize = 16.sp,
                            color = textColor
                        )
                    )

                    BoxWithConstraints(
                        modifier = Modifier
                            .width(120.dp)
                            .height(48.dp)
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = Color.White.copy(alpha = .95f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(width = this.maxWidth, height = this.maxHeight)
                                .padding(horizontal = 4.dp),
                            painter = painter,
                            contentDescription = "airline_logo_icon",
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Departure & Arrival
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(.5f),
                        text = departureAirportIataCode.toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 18.sp,
                            color = textColor
                        )
                    )

                    DottedLink(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    )

                    Text(
                        modifier = Modifier.weight(.5f),
                        text = arrivalAirportIataCode.toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.End
                        )
                    )
                }

                // Flight status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = flightStatus.toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp,
                            color = textColor
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun InfoContainerTitleDescription(
    modifier: Modifier = Modifier,
    title: NotBlankString,
    description: NotBlankString,
    isRightSide: Boolean = false
) {
    TheLabTheme {
        Column(
            modifier = Modifier.then(modifier),
            horizontalAlignment = if (isRightSide) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = title.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.W300,
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    textAlign = if (isRightSide) TextAlign.End else TextAlign.Start
                )
            )
            Text(
                text = description.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.W600,
                    fontSize = 15.sp,
                    color = textColor,
                    textAlign = if (isRightSide) TextAlign.End else TextAlign.Start
                )
            )
        }
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun FlightInfoContainer(
    airline: NotBlankString,
    aircraftType: NotBlankString,
    estimatedDepartureDate: NotBlankString,
    estimatedDepartureTime: NotBlankString,
    estimatedArrivalDate: NotBlankString,
    estimatedArrivalTime: NotBlankString,
    actualDepartureDate: NotBlankString,
    actualDepartureTime: NotBlankString,
    actualArrivalDate: NotBlankString,
    actualArrivalTime: NotBlankString,
) {
    val locale by remember { mutableStateOf(Locale.getDefault()) }
    val formatter = DateTimeFormatter.ofPattern(SearchFlightActivity.DATE_FORMAT_PATTERN, locale)

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = cardBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                // Airline & Aircraft Type
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoContainerTitleDescription(
                        title = NotBlankString.create("Airline"),
                        description = airline
                    )

                    InfoContainerTitleDescription(
                        title = NotBlankString.create("Aircraft type"),
                        description = aircraftType,
                        isRightSide = true
                    )
                }

                // Estimated Departure / Arrival date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoContainerTitleDescription(
                        modifier = Modifier.weight(1f),
                        title = NotBlankString.create("Estimated Departure date"),
                        description = NotBlankString.create(
                            if (estimatedDepartureDate.toString() == "N/A") "N/A" else estimatedDepartureDate.toLocalDateTime()
                                .format(formatter).toString()
                        )
                    )

                    InfoContainerTitleDescription(
                        modifier = Modifier.weight(1f),
                        title = NotBlankString.create("Estimated Arrival date"),
                        description = NotBlankString.create(
                            if (estimatedArrivalDate.toString() == "N/A") "N/A" else estimatedArrivalDate.toLocalDateTime()
                                .format(formatter).toString()
                        ),
                        isRightSide = true
                    )
                }

                // Estimated Departure / Arrival time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    InfoContainerTitleDescription(
                        title = NotBlankString.create("Departure time"),
                        description = NotBlankString.create(
                            if (estimatedDepartureTime.toString() == "N/A") "N/A" else estimatedDepartureTime.toLocalDateTime()
                                .toLocalTime().toString()
                        )
                    )

                    InfoContainerTitleDescription(
                        title = NotBlankString.create("Arrival time"),
                        description = NotBlankString.create(
                            if (estimatedArrivalTime.toString() == "N/A") "N/A" else estimatedArrivalTime.toLocalDateTime()
                                .toLocalTime().toString()
                        ),
                        isRightSide = true
                    )
                }

                // Actual Departure / Arrival date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoContainerTitleDescription(
                        modifier = Modifier.weight(1f),
                        title = NotBlankString.create("Actual Departure date"),
                        description = NotBlankString.create(
                            if (actualDepartureDate.toString() == "N/A") "N/A" else actualDepartureDate.toLocalDateTime()
                                .format(formatter).toString()
                        )
                    )

                    InfoContainerTitleDescription(
                        modifier = Modifier.weight(1f),
                        title = NotBlankString.create("Actual Arrival date"),
                        description = NotBlankString.create(
                            if (actualArrivalDate.toString() == "N/A") "N/A" else actualArrivalDate.toLocalDateTime()
                                .format(formatter).toString()
                        ),
                        isRightSide = true
                    )
                }

                // Actual Departure / Arrival time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    InfoContainerTitleDescription(
                        title = NotBlankString.create("Actual Departure time"),
                        description = NotBlankString.create(
                            if (actualDepartureTime.toString() == "N/A") "N/A" else actualDepartureTime.toLocalDateTime()
                                .toLocalTime().toString()
                        )
                    )

                    InfoContainerTitleDescription(
                        title = NotBlankString.create("Actual Arrival time"),
                        description = NotBlankString.create(
                            if (actualArrivalTime.toString() == "N/A") "N/A" else actualArrivalTime.toLocalDateTime()
                                .toLocalTime().toString()
                        ),
                        isRightSide = true
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@Composable
fun FlightDetailSuccessContent(flight: SearchFlightModel) {
    val lazyListState = rememberLazyListState()

    // this is to disable the ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    TheLabTheme {
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
                FlightStatusCard(
                    flightId = NotBlankString.create(
                        flight.faFlightID.toString().split("-")[0]
                    ),
                    airlineIATA = flight.identIATA ?: flight.identICAO!!,
                    departureAirportIataCode = flight.origin?.codeIcao
                        ?: NotBlankString.create("N/A"),
                    arrivalAirportIataCode = flight.destination?.codeIcao
                        ?: NotBlankString.create("N/A"),
                    flightStatus = flight.status
                )
            }

            item {
                FlightInfoContainer(
                    airline = flight.operatorID,
                    aircraftType = flight.aircraftType ?: NotBlankString.create("N/A"),
                    estimatedDepartureDate = flight.estimatedOut
                        ?: NotBlankString.create("N/A"),
                    estimatedDepartureTime = flight.estimatedOut
                        ?: NotBlankString.create("N/A"),
                    estimatedArrivalDate = flight.estimatedIn
                        ?: NotBlankString.create("N/A"),
                    estimatedArrivalTime = flight.estimatedIn
                        ?: NotBlankString.create("N/A"),
                    actualDepartureDate = flight.actualOut ?: NotBlankString.create("N/A"),
                    actualDepartureTime = flight.actualOut ?: NotBlankString.create("N/A"),
                    actualArrivalDate = flight.actualIn ?: NotBlankString.create("N/A"),
                    actualArrivalTime = flight.actualIn ?: NotBlankString.create("N/A"),
                )
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
private fun PreviewFlightStatusCard(@PreviewParameter(PreviewProviderFlight::class) flight: SearchFlightModel) {
    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            FlightStatusCard(
                flightId = NotBlankString.create(flight.faFlightID.toString().split("-")[0]),
                airlineIATA = flight.operatorID,
                departureAirportIataCode = flight.origin?.codeIata ?: NotBlankString.create("N/A"),
                arrivalAirportIataCode = flight.destination?.codeIata
                    ?: NotBlankString.create("N/A"),
                flightStatus = flight.status
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewInfoContainerTitleDescription(@PreviewParameter(PreviewProviderFlight::class) flight: SearchFlightModel) {
    TheLabTheme {
        InfoContainerTitleDescription(
            modifier = Modifier.background(backgroundColor),
            title = flight.estimatedOut!!,
            description = flight.scheduledOut!!
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewInfoContainerTitleDescriptionRightSide(@PreviewParameter(PreviewProviderFlight::class) flight: SearchFlightModel) {
    TheLabTheme {
        InfoContainerTitleDescription(
            modifier = Modifier.background(backgroundColor),
            title = flight.estimatedIn!!,
            description = flight.scheduledIn!!,
            isRightSide = true
        )
    }
}

@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewFlightInfoContainer(@PreviewParameter(PreviewProviderFlight::class) flight: SearchFlightModel) {
    TheLabTheme {
        FlightInfoContainer(
            airline = flight.operatorID,
            aircraftType = flight.aircraftType ?: NotBlankString.create("N/A"),
            estimatedDepartureDate = flight.estimatedOut
                ?: NotBlankString.create("N/A"),
            estimatedDepartureTime = flight.estimatedOut
                ?: NotBlankString.create("N/A"),
            estimatedArrivalDate = flight.estimatedIn
                ?: NotBlankString.create("N/A"),
            estimatedArrivalTime = flight.estimatedIn
                ?: NotBlankString.create("N/A"),
            actualDepartureDate = flight.actualOut ?: NotBlankString.create("N/A"),
            actualDepartureTime = flight.actualOut ?: NotBlankString.create("N/A"),
            actualArrivalDate = flight.actualIn ?: NotBlankString.create("N/A"),
            actualArrivalTime = flight.actualIn ?: NotBlankString.create("N/A"),
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewFlightDetailSuccessContent(@PreviewParameter(PreviewProviderFlight::class) flight: SearchFlightModel) {
    TheLabTheme {
        FlightDetailSuccessContent(flight = flight)
    }
}