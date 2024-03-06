package com.riders.thelab.feature.flightaware.ui.main

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.data.local.model.flight.OperatorModel

class PreviewProviderAirport : PreviewParameterProvider<AirportModel> {
    override val values: Sequence<AirportModel>
        get() = sequenceOf(
            AirportModel(
                airportId = null,
                alternateId = "01LA",
                icaoCode = null,
                iataCode = null,
                lidCode = "01LA",
                name = "Barham",
                type = "Airport",
                elevation = "90",
                city = "Oak Ridge",
                state = "LA",
                longitude = -91.7737361,
                latitude = 32.6390269,
                timezone = "America/Chicago",
                wikiUrl = null,
                airportFlightUrl = "/airports/01LA/flights"
            )
        )
}

class PreviewProviderOperator : PreviewParameterProvider<OperatorModel> {
    override val values: Sequence<OperatorModel>
        get() = sequenceOf(
            OperatorModel(
                name = "Asiana Airlines",
                shortname = "Asiana",
                city = null,
                country = "South Korea",
                icao = "AAR",
                iata = "OZ",
                callsign = "Asiana",
                location = "Republic Of Korea",
                phone = "+1-800-227-4262",
                url = "http://us.flyasiana.com/",
                wikiUrl = "https://en.wikipedia.org/wiki/Asiana_Airlines",
                alternatives = emptyList()
            )
        )
}