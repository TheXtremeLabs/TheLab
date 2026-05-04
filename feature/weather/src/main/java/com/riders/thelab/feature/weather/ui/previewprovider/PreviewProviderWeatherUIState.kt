package com.riders.thelab.feature.weather.ui.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.feature.weather.data.compose.WeatherUiModel
import com.riders.thelab.feature.weather.data.compose.WeatherUiState


class PreviewProviderWeatherUIState : PreviewParameterProvider<WeatherUiState> {
    override val values: Sequence<WeatherUiState>
        get() = sequenceOf(
            WeatherUiState.None,
            WeatherUiState.Success(
                model = WeatherUiModel(
                    cities = listOf(PreviewProviderCity().values.first()),
                    weather = PreviewProviderWeather().values.first()
                )
            ),
            WeatherUiState.Error(message = "Error"),
            WeatherUiState.Loading(message = "Loading")
        )
}