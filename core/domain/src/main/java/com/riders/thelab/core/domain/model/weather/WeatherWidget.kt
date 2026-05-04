package com.riders.thelab.core.domain.model.weather

import com.riders.thelab.core.common.utils.DateTimeUtils
import java.io.Serializable

@kotlinx.serialization.Serializable
data class WeatherWidget(
    val description: String,
    var icon: String,
    val temperature: Temperature,
    val forecast: List<ForecastWeatherWidget>
) : Serializable

fun Weather.toWidgetModel(): WeatherWidget? {
    var minStoredTemperature: Double? = null
    var maxStoredTemperature: Double? = null

    this@toWidgetModel.hourlyWeather?.let {

        minStoredTemperature = it[0].temperature?.min
        maxStoredTemperature = it[0].temperature?.max

        it.forEach { temp ->
            if (minStoredTemperature!! >= temp.temperature?.min!!) {
                minStoredTemperature = temp.temperature.min
            }
        }

        it.forEach { temp ->
            if (temp.temperature?.max!! >= maxStoredTemperature!!) {
                maxStoredTemperature = temp.temperature.max
            }
        }
    }

    val temperature = Temperature(
        temperature = this.temperature?.temperature!!,
        realFeels = this.feelsLike?.feelsLike!!,
        min = minStoredTemperature ?: 0.0,
        max = maxStoredTemperature ?: 0.0
    )

    val description: String? = this.description
    val icon: String? = this.weatherIconUrl

    val dailyWeather: List<ForecastWeatherWidget>? = this@toWidgetModel.dailyWeather?.run {
        this.map {
            ForecastWeatherWidget(
                day = DateTimeUtils.getDayFromTime(it.dateTimeUTC),
                temperature = it.temperature!!,
                icon = it.weatherIconUrl!!
            )
        }.toList()
    }

    return if (null == description || null == icon || null == dailyWeather) {
        null
    } else {
        WeatherWidget(
            description = description,
            icon = icon,
            temperature = temperature,
            forecast = dailyWeather
        )
    }
}