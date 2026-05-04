package com.riders.thelab.feature.weather.ui.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.domain.model.weather.Weather


class PreviewProviderWeather : PreviewParameterProvider<Weather> {
    override val values: Sequence<Weather> get() = sequenceOf(Weather.mockWeather)
}