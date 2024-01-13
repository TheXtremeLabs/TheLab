package com.riders.thelab.feature.weather.core.widget

import com.riders.thelab.core.data.local.model.weather.WeatherWidgetModel
import kotlinx.serialization.Serializable

@Serializable
sealed interface WeatherInfo {
    @Serializable
    data object Loading : WeatherInfo

    @Serializable
    data class Available(
        val placeName: String,
        val currentData: WeatherWidgetModel
    ) : WeatherInfo

    @Serializable
    data class Unavailable(val message: String) : WeatherInfo
}