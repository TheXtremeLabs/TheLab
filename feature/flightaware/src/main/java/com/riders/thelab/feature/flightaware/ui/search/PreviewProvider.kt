package com.riders.thelab.feature.flightaware.ui.search

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.data.local.model.flight.OriginDestinationModel
import com.riders.thelab.core.data.local.model.flight.SegmentModel
import com.riders.thelab.core.data.remote.dto.flight.FlightType
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

class PreviewProviderFlight : PreviewParameterProvider<FlightModel> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<FlightModel>
        get() = sequenceOf(
            FlightModel(
                operatorID = NotBlankString.create("AAL306"),
                identICAO = NotBlankString.create("AAL306"),
                identIATA = NotBlankString.create("AA306"),
                faFlightID = NotBlankString.create("AAL306-1712349302-airline-801p"),
                origin = OriginDestinationModel(
                    codeIcao = NotBlankString.create("KJFK"),
                    codeIata = NotBlankString.create("JFK"),
                    codeLid = NotBlankString.create("JFK"),
                    timezone = NotBlankString.create("America/New_York"),
                    name = NotBlankString.create("Int'l John-F.-Kennedy"),
                    city = NotBlankString.create("New York"),
                    airportInfoUrl = NotBlankString.create("/airports/KJFK")
                ),
                destination = OriginDestinationModel(
                    codeIcao = NotBlankString.create("KLAX"),
                    codeIata = NotBlankString.create("LAX"),
                    codeLid = NotBlankString.create("LAX"),
                    timezone = NotBlankString.create("America/Los_Angeles"),
                    name = NotBlankString.create("Int'l de Los Angeles"),
                    city = NotBlankString.create("Los Angeles"),
                    airportInfoUrl = NotBlankString.create("/airports/KLAX")
                ),
                waypoints = null,
                firstTimePosition = null,
                boundingBox = null,
                identPrefix = null,
                aircraftType = NotBlankString.create("A321"),
                progress = 20,
                status = NotBlankString.create("Planifié"),
                actualOff = null,
                actualOn = null,
                foresightPredictionsAvailable = false,
                segments = listOf(
                    SegmentModel(
                        operatorID = NotBlankString.create("AFR25"),
                        identICAO = NotBlankString.create("AFR25"),
                        identIATA = NotBlankString.create("AF25"),
                        actualRunwayOff = null,
                        actualRunwayOn = null,
                        faFlightID = NotBlankString.create("AFR25-1714095213-schedule-1763p"),
                        operator = NotBlankString.create("AFR"),
                        operatorICAO = NotBlankString.create("AFR"),
                        operatorIATA = NotBlankString.create("AF"),
                        flightNumber = NotBlankString.create("25"),
                        registration = null,
                        atcIdent = null,
                        inboundFaFlightID = null,
                        /*codeshares= emptyList(),
                        codeSharesIata= emptyList(),*/
                        blocked = false,
                        diverted = false,
                        cancelled = false,
                        positionOnly = false,
                        origin = OriginDestinationModel(
                            codeIcao = NotBlankString.create("KLAX"),
                            codeIata = NotBlankString.create("LAX"),
                            codeLid = null,
                            timezone = NotBlankString.create("America/Los_Angeles"),
                            name = NotBlankString.create("Int'l de Los Angeles"),
                            city = NotBlankString.create("Los Angeles"),
                            airportInfoUrl = NotBlankString.create("/airports/KLAX")
                        ),
                        destination = OriginDestinationModel(
                            codeIcao = NotBlankString.create("LFPG"),
                            codeIata = NotBlankString.create("CDG"),
                            codeLid = null,
                            timezone = NotBlankString.create("Europe/Paris"),
                            name = NotBlankString.create("Paris-Charles-de-Gaulle"),
                            city = NotBlankString.create("Paris"),
                            airportInfoUrl = NotBlankString.create("/airports/LFPG")
                        ),
                        departureDelay = 0,
                        arrivalDelay = 0,
                        filedEte = 37800,
                        scheduledOut = NotBlankString.create("2024-04-28T01:30:00Z"),
                        estimatedOut = NotBlankString.create("2024-04-28T01:30:00Z"),
                        actualOut = null,
                        scheduledOff = NotBlankString.create("2024-04-28T01:40:00Z"),
                        estimatedOff = NotBlankString.create("2024-04-28T01:40:00Z"),
                        actualOff = null,
                        scheduledOn = NotBlankString.create("2024-04-28T12:10:00Z"),
                        estimatedOn = NotBlankString.create("2024-04-28T12:10:00Z"),
                        actualOn = null,
                        scheduledIn = NotBlankString.create("2024-04-28T12:20:00Z"),
                        estimatedIn = NotBlankString.create("2024-04-28T12:20:00Z"),
                        actualIn = null,
                        progress = 0,
                        status = NotBlankString.create("Planifié"),
                        aircraftType = NotBlankString.create("B77W"),
                        routeDistance = 5675,
                        filedAirSpeed = 496,
                        filedAltitude = 330,
                        route = NotBlankString.create("DTA039023 KU78S KU06W 4900N/10000W 5430N/09000W 5730N/08000W 5930N/07000W RODBO PIDSO 6100N/05000W 6100N/04000W 6000N/03000W 5800N/02000W PIKIL SOVED REVNU MORAG NUCHU L18 MID Y803 SFD UM605 BIBAX BIBAX9W"),
                        baggageClaim = NotBlankString.create("30"),
                        seatsCabinBusiness = 58,
                        seatsCabinCoach = 234,
                        seatsCabinFirst = 4,
                        gateOrigin = NotBlankString.create("208"),
                        gateDestination = null,
                        terminalOrigin = NotBlankString.create("B"),
                        terminalDestination = NotBlankString.create("2E"),
                        type = FlightType.AIRLINE
                    )
                )
            )
        )
}