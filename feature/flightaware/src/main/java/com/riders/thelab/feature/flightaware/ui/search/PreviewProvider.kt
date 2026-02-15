package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.flight.OriginDestinationModel
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.core.data.remote.dto.flight.FlightType
import com.riders.thelab.feature.flightaware.data.local.model.SearchFlightType
import com.riders.thelab.feature.flightaware.data.local.model.compose.SearchFlightsUiState
import com.riders.thelab.feature.flightaware.ui.flight.PreviewProviderFlight
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi

class PreviewProviderSearchFlightsUiState : PreviewParameterProvider<SearchFlightsUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<SearchFlightsUiState>
        get() = sequenceOf(
            SearchFlightsUiState.Loading(
                searchType = SearchFlightType.NUMBER,
                message = "Search flights for AAL306".toNotBlankString().getOrThrow()
            ),
            SearchFlightsUiState.Loading(
                searchType = SearchFlightType.ROUTE,
                message = "Search flights for route\n\"ORY\" to \"LAX\"".toNotBlankString()
                    .getOrThrow()
            ),
            SearchFlightsUiState.Error(
                message = "Error occurred while getting value".toNotBlankString().getOrThrow()
            ),
            SearchFlightsUiState.Success(PreviewProviderFlight().values.toList()),
        )
}

class PreviewProviderFlight : PreviewParameterProvider<SearchFlightModel> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<SearchFlightModel>
        get() = sequenceOf(
            SearchFlightModel(
                operatorID = "AAL306".toNotBlankString().getOrThrow(),
                operatorICAO = "AAL306".toNotBlankString().getOrThrow(),
                operatorIATA = "AA306".toNotBlankString().getOrThrow(),
                faFlightID = "AAL306-1712349302-airline-801p".toNotBlankString().getOrThrow(),
                origin = OriginDestinationModel(
                    codeIcao = "KJFK".toNotBlankString().getOrThrow(),
                    codeIata = "JFK".toNotBlankString().getOrThrow(),
                    codeLid = "JFK".toNotBlankString().getOrThrow(),
                    timezone = "America/New_York".toNotBlankString().getOrThrow(),
                    name = "Int'l John-F.-Kennedy".toNotBlankString().getOrThrow(),
                    city = "New York".toNotBlankString().getOrThrow(),
                    airportInfoUrl = "/airports/KJFK".toNotBlankString().getOrThrow(),
                ),
                destination = OriginDestinationModel(
                    codeIcao = "KLAX".toNotBlankString().getOrThrow(),
                    codeIata = "LAX".toNotBlankString().getOrThrow(),
                    codeLid = "LAX".toNotBlankString().getOrThrow(),
                    timezone = "America/Los_Angeles".toNotBlankString().getOrThrow(),
                    name = "Int'l de Los Angeles".toNotBlankString().getOrThrow(),
                    city = "Los Angeles".toNotBlankString().getOrThrow(),
                    airportInfoUrl = "/airports/KLAX".toNotBlankString().getOrThrow(),
                ),
                waypoints = null,
                firstTimePosition = null,
                boundingBox = null,
                identPrefix = null,
                aircraftType = "A321".toNotBlankString().getOrThrow(),
                progress = 20,
                status = "Planifié".toNotBlankString().getOrThrow(),
                actualOff = null,
                actualOn = null,
                foresightPredictionsAvailable = false,
                type = FlightType.AIRLINE
            ),
            SearchFlightModel(
                operatorName = "AFR".toNotBlankString().getOrThrow(),
                operatorID = "AFR25".toNotBlankString().getOrThrow(),
                operatorICAO = "AFR".toNotBlankString().getOrThrow(),
                operatorIATA = "AF".toNotBlankString().getOrThrow(),
                actualRunwayOff = null,
                actualRunwayOn = null,
                faFlightID = "AFR25-1714095213-schedule-1763p".toNotBlankString().getOrThrow(),
                flightNumber = "25".toNotBlankString().getOrThrow(),
                registration = null,
                atcIdent = null,
                inboundFaFlightID = null,
                /*codeshares= emptyList(.toNotBlankString().getOrThrow(),
                codeSharesIata= emptyList(.toNotBlankString().getOrThrow(),*/
                blocked = false,
                diverted = false,
                cancelled = false,
                positionOnly = false,
                origin = OriginDestinationModel(
                    codeIcao = "KLAX".toNotBlankString().getOrThrow(),
                    codeIata = "LAX".toNotBlankString().getOrThrow(),
                    codeLid = null,
                    timezone = "America/Los_Angeles".toNotBlankString().getOrThrow(),
                    name = "Int'l de Los Angeles".toNotBlankString().getOrThrow(),
                    city = "Los Angeles".toNotBlankString().getOrThrow(),
                    airportInfoUrl = "/airports/KLAX".toNotBlankString().getOrThrow(),
                ),
                destination = OriginDestinationModel(
                    codeIcao = "LFPG".toNotBlankString().getOrThrow(),
                    codeIata = "CDG".toNotBlankString().getOrThrow(),
                    codeLid = null,
                    timezone = "Europe/Paris".toNotBlankString().getOrThrow(),
                    name = "Paris-Charles-de-Gaulle".toNotBlankString().getOrThrow(),
                    city = "Paris".toNotBlankString().getOrThrow(),
                    airportInfoUrl = "/airports/LFPG".toNotBlankString().getOrThrow(),
                ),
                departureDelay = 0,
                arrivalDelay = 0,
                filedEte = 37800,
                scheduledOut = "2024-04-28T01:30:00Z".toNotBlankString().getOrThrow(),
                estimatedOut = "2024-04-28T01:30:00Z".toNotBlankString().getOrThrow(),
                actualOut = null,
                scheduledOff = "2024-04-28T01:40:00Z".toNotBlankString().getOrThrow(),
                estimatedOff = "2024-04-28T01:40:00Z".toNotBlankString().getOrThrow(),
                actualOff = null,
                scheduledOn = "2024-04-28T12:10:00Z".toNotBlankString().getOrThrow(),
                estimatedOn = "2024-04-28T12:10:00Z".toNotBlankString().getOrThrow(),
                actualOn = null,
                scheduledIn = "2024-04-28T12:20:00Z".toNotBlankString().getOrThrow(),
                estimatedIn = "2024-04-28T12:20:00Z".toNotBlankString().getOrThrow(),
                actualIn = null,
                progress = 0,
                status = "Planifié".toNotBlankString().getOrThrow(),
                aircraftType = "B77W".toNotBlankString().getOrThrow(),
                routeDistance = 5675,
                filedAirSpeed = 496,
                filedAltitude = 330,
                route = "DTA039023 KU78S KU06W 4900N/10000W 5430N/09000W 5730N/08000W 5930N/07000W RODBO PIDSO 6100N/05000W 6100N/04000W 6000N/03000W 5800N/02000W PIKIL SOVED REVNU MORAG NUCHU L18 MID Y803 SFD UM605 BIBAX BIBAX9W".toNotBlankString()
                    .getOrThrow(),
                baggageClaim = "30".toNotBlankString().getOrThrow(),
                seatsCabinBusiness = 58,
                seatsCabinCoach = 234,
                seatsCabinFirst = 4,
                gateOrigin = "208".toNotBlankString().getOrThrow(),
                gateDestination = null,
                terminalOrigin = "B".toNotBlankString().getOrThrow(),
                terminalDestination = "2E".toNotBlankString().getOrThrow(),
                type = FlightType.AIRLINE
            )
        )
}

class PreviewProviderFlights : PreviewParameterProvider<List<SearchFlightModel>> {
    override val values: Sequence<List<SearchFlightModel>>
        get() = sequenceOf(PreviewProviderFlight().values.toList())
}