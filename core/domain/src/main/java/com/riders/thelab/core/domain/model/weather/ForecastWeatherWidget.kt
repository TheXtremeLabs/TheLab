package com.riders.thelab.core.domain.model.weather

@kotlinx.serialization.Serializable
data class ForecastWeatherWidget(
    val day: String,
    val temperature: Temperature,
    var icon: String
)