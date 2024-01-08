package com.riders.thelab.core.data.local.model.weather

import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.core.data.remote.dto.weather.toModel
import java.io.Serializable

@kotlinx.serialization.Serializable
data class WeatherWidgetModel(
    val temperature: TemperatureModel,
    var icon: String,
    val description: String,
    val forecast: List<ForecastWeatherWidgetModel>
) : Serializable {
    constructor() : this(TemperatureModel(), "", "", emptyList())

    override fun toString(): String {
        return "temperature: $temperature, icon: $icon, description: $description, forecast: size:${forecast.size}, content: ${forecast.toString()}"
    }
}

fun OneCallWeatherResponse.toWidgetModel(): WeatherWidgetModel? = this.currentWeather?.run {
    val temperature =
        TemperatureModel(temperature = this.temperature, realFeels = this.feelsLike)

    val description: String? = this.weather?.get(0)?.description
    val icon: String? = this.weather?.get(0)?.icon

    val dailyWeather: List<ForecastWeatherWidgetModel>? = this@toWidgetModel.dailyWeather?.run {
        this.map {
            ForecastWeatherWidgetModel(
                it.temperature.toModel(),
                it.weather[0].icon
            )
        }.toList()
    }

    if (null == description || null == icon || null == dailyWeather) {
        return null
    } else {
        return WeatherWidgetModel(temperature, icon, description, dailyWeather)
    }
}

