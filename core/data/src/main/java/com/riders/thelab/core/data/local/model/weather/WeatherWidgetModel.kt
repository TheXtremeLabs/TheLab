package com.riders.thelab.core.data.local.model.weather

import java.io.Serializable

@kotlinx.serialization.Serializable
data class WeatherWidgetModel(
    val temperature: TemperatureModel,
    val icon: String,
    val description: String,
    val forecast: List<ForecastWeatherWidgetModel>
) : Serializable {
    constructor() : this(TemperatureModel(), "", "", emptyList())

    override fun toString(): String {
        return "temperature: $temperature, icon: $icon, description: $description, forecast: size:${forecast.size}, content: ${forecast.toString()}"
    }
}
