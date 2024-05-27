package com.riders.thelab.core.data.local.model.compose.weather

import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse

sealed class WeatherCityUIState {
    data class Success(val weather: OneCallWeatherResponse) : WeatherCityUIState()
    data class Error(val errorResponse: Throwable? = null) : WeatherCityUIState()
    data object None : WeatherCityUIState()
}