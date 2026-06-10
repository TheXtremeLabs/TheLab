package com.riders.thelab.feature.weather.data.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class WeatherUiState {

    @Stable
    data class Success(val model: WeatherUiModel) : WeatherUiState()

    @Stable
    @Immutable
    data class Error(val message: String, val errorResponse: Throwable? = null) : WeatherUiState()

    @Stable
    @Immutable
    data object NoDataFound : WeatherUiState()

    @Stable
    data class Loading(val message: String) : WeatherUiState()

    @Stable
    @Immutable
    data object None : WeatherUiState()

}