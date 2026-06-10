package com.riders.thelab.feature.weather.data.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.domain.model.weather.WeatherWidget
import kotlinx.serialization.Serializable

@Serializable
@Stable
sealed interface WeatherInfoUiState {

    @Serializable
    @Stable
    @Immutable
    data class Available(
        val placeName: String,
        val currentData: WeatherWidget
    ) : WeatherInfoUiState

    @Serializable
    @Stable
    @Immutable
    data class Unavailable(val message: String) : WeatherInfoUiState

    @Serializable
    @Stable
    @Immutable
    data object Loading : WeatherInfoUiState
}