package com.riders.thelab.core.data.local.model.compose.weather

import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse

sealed class WeatherUIState {
    data class SuccessWeatherData(val isWeatherData: Boolean) : WeatherUIState()
    data class Success(val weather: OneCallWeatherResponse) : WeatherUIState()
    data class Error(val errorResponse: Throwable? = null) : WeatherUIState()
    data object Loading : WeatherUIState()
    data object None : WeatherUIState()
}