package com.riders.thelab.core.data.local.model.compose

import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse

sealed class WeatherUIState {
    data class SuccessWeatherData(val isWeatherData: Boolean) : WeatherUIState()
    data class Success(val weather: OneCallWeatherResponse) : WeatherUIState()
    data class Error(val errorResponse: Throwable? = null) : WeatherUIState()
    object Loading : WeatherUIState()
    object None : WeatherUIState()
}