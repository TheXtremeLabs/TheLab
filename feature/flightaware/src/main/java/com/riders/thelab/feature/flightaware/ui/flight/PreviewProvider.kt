package com.riders.thelab.feature.flightaware.ui.flight

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.data.local.model.flight.OriginDestinationModel
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
                foresightPredictionsAvailable = false,
                segments = null
            )
        )
}