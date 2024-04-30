package com.riders.thelab.feature.flightaware.ui.flight

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.FlightDetailUiState
import com.riders.thelab.core.data.local.model.flight.OriginDestinationModel
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.core.data.remote.dto.flight.FlightType
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

class PreviewProviderUiState : PreviewParameterProvider<FlightDetailUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<FlightDetailUiState>
        get() = sequenceOf(
            FlightDetailUiState.Loading,
            FlightDetailUiState.Error(NotBlankString.create("Error occurred while getting value")),
            FlightDetailUiState.Success(PreviewProviderFlight().values.toList()[0]),
        )
}

class PreviewProviderFlight : PreviewParameterProvider<SearchFlightModel> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<SearchFlightModel>
        get() = sequenceOf(
            SearchFlightModel(
                operatorName = NotBlankString.create("American Airlines"),
                operatorID = NotBlankString.create("AAL306"),
                operatorICAO = NotBlankString.create("AAL306"),
                operatorIATA = NotBlankString.create("AA306"),
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
                type = FlightType.AIRLINE,
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
            )
        )
}