package com.riders.thelab.data.local.model.compose

import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse

sealed class WeatherUIState {
    data class SuccessWeatherData(val isWeatherData: Boolean) : WeatherUIState()
    data class Success(val weather: OneCallWeatherResponse) : WeatherUIState()
    data class Error(val errorResponse: Throwable? = null) : WeatherUIState()
    object Loading : WeatherUIState()
    object None : WeatherUIState()
}