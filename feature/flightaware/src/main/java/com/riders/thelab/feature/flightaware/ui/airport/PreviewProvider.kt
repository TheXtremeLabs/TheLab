package com.riders.thelab.feature.flightaware.ui.airport

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import com.riders.thelab.core.data.remote.dto.flight.Arrivals
import com.riders.thelab.core.data.remote.dto.flight.Departures
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

class PreviewProviderAirportSearch : PreviewParameterProvider<AirportSearchModel> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<AirportSearchModel>
        get() = sequenceOf(
            AirportSearchModel(
                name = NotBlankString.create("Los Angeles Intl (Los Angeles)"),
                description = NotBlankString.create("Los Angeles Intl (Los Angeles)"),
                icaoCode = NotBlankString.create("KLAX"),
                iataCode = NotBlankString.create("LAX")
            ),
            AirportSearchModel(
                name = NotBlankString.create("Whiteman (Los Angeles)"),
                description = NotBlankString.create("Whiteman (Los Angeles)"),
                icaoCode = NotBlankString.create("KWHP"),
                iataCode = NotBlankString.create("WHP")
            ),
            AirportSearchModel(
                name = NotBlankString.create("María Dolores (Los Angeles)"),
                description = NotBlankString.create("María Dolores (Los Angeles)"),
                icaoCode = NotBlankString.create("SCGE")
            ),
            AirportSearchModel(
                name = NotBlankString.create("Los Cabos Int'l (Los Cabos)"),
                description = NotBlankString.create("Los Cabos Int'l (Los Cabos)"),
                icaoCode = NotBlankString.create("MMSD"),
                iataCode = NotBlankString.create("SJD")
            ),
            AirportSearchModel(
                name = NotBlankString.create("Southern Wisconsin Rgnl (Janesville)"),
                description = NotBlankString.create("Southern Wisconsin Rgnl (Janesville)"),
                icaoCode = NotBlankString.create("KJVL"),
                iataCode = NotBlankString.create("JVL")
            )
        )
}

class PreviewProviderAirports : PreviewParameterProvider<List<AirportSearchModel>> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<List<AirportSearchModel>>
        get() = sequenceOf(
            listOf(
                AirportSearchModel(
                    name = NotBlankString.create("Los Angeles Intl (Los Angeles)"),
                    description = NotBlankString.create("Los Angeles Intl (Los Angeles)"),
                    icaoCode = NotBlankString.create("KLAX"),
                    iataCode = NotBlankString.create("LAX")
                ),
                AirportSearchModel(
                    name = NotBlankString.create("Whiteman (Los Angeles)"),
                    description = NotBlankString.create("Whiteman (Los Angeles)"),
                    icaoCode = NotBlankString.create("KWHP"),
                    iataCode = NotBlankString.create("WHP")
                ),
                AirportSearchModel(
                    name = NotBlankString.create("María Dolores (Los Angeles)"),
                    description = NotBlankString.create("María Dolores (Los Angeles)"),
                    icaoCode = NotBlankString.create("SCGE")
                ),
                AirportSearchModel(
                    name = NotBlankString.create("Los Cabos Int'l (Los Cabos)"),
                    description = NotBlankString.create("Los Cabos Int'l (Los Cabos)"),
                    icaoCode = NotBlankString.create("MMSD"),
                    iataCode = NotBlankString.create("SJD")
                ),
                AirportSearchModel(
                    name = NotBlankString.create("Southern Wisconsin Rgnl (Janesville)"),
                    description = NotBlankString.create("Southern Wisconsin Rgnl (Janesville)"),
                    icaoCode = NotBlankString.create("KJVL"),
                    iataCode = NotBlankString.create("JVL")
                )
            )
        )
}