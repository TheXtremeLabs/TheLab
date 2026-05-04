package com.riders.thelab.feature.weather.data.compose

import androidx.compose.runtime.Stable
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.model.weather.Weather

@Stable
data class WeatherUiModel(
    val cities: List<City>,
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
