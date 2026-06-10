package com.riders.thelab.feature.weather.ui.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.model.weather.Coordinates
import java.util.UUID

class PreviewProviderCity : PreviewParameterProvider<City> {
    override val values: Sequence<City>
        get() = sequenceOf(
            City(
                name = "Paris",
                country = "France",
                coordinates = Coordinates(
                    latitude = 48.8534,
                    longitude = 2.3486
                )
            ),
            City(
                uuid = UUID.randomUUID().toString(),
                name = "Johanesburg",
                state = "",
                country = "South Africa",
                coordinates = Coordinates(
                    longitude = 48.3535,
                    latitude = 3.58978
                )
            )
        )
}