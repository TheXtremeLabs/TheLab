package com.riders.thelab.feature.flightaware.ui.flight

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.flight.FlightDetailUiState
import com.riders.thelab.core.data.local.model.flight.OriginDestinationModel
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.core.data.remote.dto.flight.FlightType
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi

class PreviewProviderFlightDetailUiState : PreviewParameterProvider<FlightDetailUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<FlightDetailUiState>
        get() = sequenceOf(
            FlightDetailUiState.Loading,
            FlightDetailUiState.Error(
                "Error occurred while getting value".toNotBlankString().getOrThrow()
            ),
            FlightDetailUiState.Success(PreviewProviderFlight().values.toList()[0]),
        )
}

class PreviewProviderFlight : PreviewParameterProvider<SearchFlightModel> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<SearchFlightModel>
        get() = sequenceOf(
            SearchFlightModel(
                operatorName = "American Airlines".toNotBlankString().getOrThrow(),
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
                    airportInfoUrl = "/airports/KJFK".toNotBlankString().getOrThrow()
                ),
                destination = OriginDestinationModel(
                    codeIcao = "KLAX".toNotBlankString().getOrThrow(),
                    codeIata = "LAX".toNotBlankString().getOrThrow(),
                    codeLid = "LAX".toNotBlankString().getOrThrow(),
                    timezone = "America/Los_Angeles".toNotBlankString().getOrThrow(),
                    name = "Int'l de Los Angeles".toNotBlankString().getOrThrow(),
                    city = "Los Angeles".toNotBlankString().getOrThrow(),
                    airportInfoUrl = "/airports/KLAX".toNotBlankString().getOrThrow()
                ),
                type = FlightType.AIRLINE,
                waypoints = null,
                firstTimePosition = null,
                boundingBox = null,
                identPrefix = null,
                aircraftType = "A321".toNotBlankString().getOrThrow(),
                progress = 20,
                status = "Planifié".toNotBlankString().getOrThrow(),
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
                foresightPredictionsAvailable = false,
            )
        )
}