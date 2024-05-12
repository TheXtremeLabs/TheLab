package com.riders.thelab.feature.youtube.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.WeatherCityUIState
import com.riders.thelab.core.data.local.model.compose.WeatherUIState
import com.riders.thelab.core.data.local.model.compose.YoutubeUiState
import com.riders.thelab.core.data.remote.dto.weather.DailyWeather
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

class PreviewProviderYoutube : PreviewParameterProvider<YoutubeUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<YoutubeUiState>
        get() = sequenceOf(
            YoutubeUiState.Loading,
            YoutubeUiState.Error(NotBlankString.create("Error occurred while getting value")),
            YoutubeUiState.Success(listOf())
        )
}
