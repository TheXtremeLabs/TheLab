package com.riders.thelab.feature.flightaware.ui.search

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
                actualOff = null,
                actualOn = null,
                foresightPredictionsAvailable = false,
                segments = null
            )
        )
}