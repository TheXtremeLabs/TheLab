package com.riders.thelab.feature.flightaware.ui.airport

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi

class PreviewProviderAirportSearch : PreviewParameterProvider<AirportSearchModel> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<AirportSearchModel>
        get() = sequenceOf(
            AirportSearchModel(
                name = "Los Angeles Intl (Los Angeles)".toNotBlankString().getOrThrow(),
                description = "Los Angeles Intl (Los Angeles)".toNotBlankString().getOrThrow(),
                icaoCode = "KLAX".toNotBlankString().getOrThrow(),
                iataCode = "LAX".toNotBlankString().getOrThrow()
            ),
            AirportSearchModel(
                name = "Paris-Charles-de-Gaulle".toNotBlankString().getOrThrow(),
                description = "Paris-Charles-de-Gaulle".toNotBlankString().getOrThrow(),
                icaoCode = "LFPG".toNotBlankString().getOrThrow(),
                iataCode = "CDG".toNotBlankString().getOrThrow(),
                city = "Paris".toNotBlankString().getOrThrow(),
            ),
            AirportSearchModel(
                name = "Whiteman (Los Angeles)".toNotBlankString().getOrThrow(),
                description = "Whiteman (Los Angeles)".toNotBlankString().getOrThrow(),
                icaoCode = "KWHP".toNotBlankString().getOrThrow(),
                iataCode = "WHP".toNotBlankString().getOrThrow(),
            ),
            AirportSearchModel(
                name = "María Dolores (Los Angeles)".toNotBlankString().getOrThrow(),
                description = "María Dolores (Los Angeles)".toNotBlankString().getOrThrow(),
                icaoCode = "SCGE".toNotBlankString().getOrThrow(),
            ),
            AirportSearchModel(
                name = "Los Cabos Int'l (Los Cabos)".toNotBlankString().getOrThrow(),
                description = "Los Cabos Int'l (Los Cabos)".toNotBlankString().getOrThrow(),
                icaoCode = "MMSD".toNotBlankString().getOrThrow(),
                iataCode = "SJD".toNotBlankString().getOrThrow(),
            ),
            AirportSearchModel(
                name = "Southern Wisconsin Rgnl (Janesville)".toNotBlankString().getOrThrow(),
                description = "Southern Wisconsin Rgnl (Janesville)".toNotBlankString()
                    .getOrThrow(),
                icaoCode = "KJVL".toNotBlankString().getOrThrow(),
                iataCode = "JVL".toNotBlankString().getOrThrow(),
            )
        )
}

class PreviewProviderAirports : PreviewParameterProvider<List<AirportSearchModel>> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<List<AirportSearchModel>>
        get() = sequenceOf(
            listOf(
                AirportSearchModel(
                    name = "Los Angeles Intl (Los Angeles)".toNotBlankString().getOrThrow(),
                    description = "Los Angeles Intl (Los Angeles)".toNotBlankString().getOrThrow(),
                    icaoCode = "KLAX".toNotBlankString().getOrThrow(),
                    iataCode = "LAX".toNotBlankString().getOrThrow(),
                ),
                AirportSearchModel(
                    name = "Whiteman (Los Angeles)".toNotBlankString().getOrThrow(),
                    description = "Whiteman (Los Angeles)".toNotBlankString().getOrThrow(),
                    icaoCode = "KWHP".toNotBlankString().getOrThrow(),
                    iataCode = "WHP".toNotBlankString().getOrThrow(),
                ),
                AirportSearchModel(
                    name = "María Dolores (Los Angeles)".toNotBlankString().getOrThrow(),
                    description = "María Dolores (Los Angeles)".toNotBlankString().getOrThrow(),
                    icaoCode = "SCGE".toNotBlankString().getOrThrow(),
                ),
                AirportSearchModel(
                    name = "Los Cabos Int'l (Los Cabos)".toNotBlankString().getOrThrow(),
                    description = "Los Cabos Int'l (Los Cabos)".toNotBlankString().getOrThrow(),
                    icaoCode = "MMSD".toNotBlankString().getOrThrow(),
                    iataCode = "SJD".toNotBlankString().getOrThrow(),
                ),
                AirportSearchModel(
                    name = "Southern Wisconsin Rgnl (Janesville)".toNotBlankString().getOrThrow(),
                    description = "Southern Wisconsin Rgnl (Janesville)".toNotBlankString()
                        .getOrThrow(),
                    icaoCode = "KJVL".toNotBlankString().getOrThrow(),
                    iataCode = "JVL".toNotBlankString().getOrThrow(),
                )
            )
        )
}