package com.riders.thelab.feature.weather.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.WeatherCityUIState
import com.riders.thelab.core.data.local.model.compose.WeatherUIState
import com.riders.thelab.core.data.remote.dto.weather.DailyWeather
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse

class PreviewProviderWeather : PreviewParameterProvider<WeatherUIState> {
    override val values: Sequence<WeatherUIState>
        get() = sequenceOf(
            WeatherUIState.Loading,
            WeatherUIState.Error(),
            WeatherUIState.Success(OneCallWeatherResponse.getMockResponse()),
            WeatherUIState.SuccessWeatherData(true),
            WeatherUIState.SuccessWeatherData(false),
        )
}

class PreviewProviderWeatherCity : PreviewParameterProvider<WeatherCityUIState> {
    override val values: Sequence<WeatherCityUIState>
        get() = sequenceOf(
            WeatherCityUIState.None,
            WeatherCityUIState.Success(OneCallWeatherResponse.getMockResponse()),
            WeatherCityUIState.Error(),
        )
}
/*
class PreviewProviderDailyWeather : PreviewParameterProvider<DailyWeather> {
    override val values: Sequence<DailyWeather>
        get() = sequenceOf(
            DailyWeather()
        )
}*/
