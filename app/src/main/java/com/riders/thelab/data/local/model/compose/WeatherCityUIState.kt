package com.riders.thelab.data.local.model.compose

import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse

sealed class WeatherCityUIState {
    data class Success(val weather: OneCallWeatherResponse) : WeatherCityUIState()
    data class Error(val errorResponse: Throwable? = null) : WeatherCityUIState()
    object None : WeatherCityUIState()
}