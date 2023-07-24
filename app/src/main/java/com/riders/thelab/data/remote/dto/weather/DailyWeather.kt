package com.riders.thelab.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class DailyWeather(
    @SerialName("dt")
    val dateTimeUTC: Long = 0,

    @SerialName("sunrise")
    val sunrise: Long = 0,

    @SerialName("sunset")
    val sunset: Long = 0,

    @SerialName("moonrise")
    val moonrise: Long = 0,

    @SerialName("moonset")
    val moonset: Long = 0,

    @SerialName("moon_phase")
    val moonPhase: Double = 0.0,

    @SerialName("temp")
    val temperature: Temperature,

    @SerialName("feels_like")
    val feelsLike: FeelsLike,

    @SerialName("pressure")
    val pressure: Int = 0,

    @SerialName("humidity")
    val humidity: Int = 0,

    @SerialName("dew_point")
    val dewPoint: Double = 0.0,

    // UV index
    @SerialName("uvi")
    val uvIndex: Double = 0.0,

    @SerialName("clouds")
    val clouds: Int = 0,

    // Average visibility, metres
    @SerialName("visibility")
    val visibility: Int = 0,

    @SerialName("wind_speed")
    val windSpeed: Double = 0.0,

    @SerialName("wind_deg")
    val windDegree: Int = 0,

    @SerialName("wind_gust")
    val windGust: Double = 0.0,

    @SerialName("weather")
    val weather: List<Weather>,

    @SerialName("rain")
    val rain: Double? = 0.0,

    @SerialName("snow")
    val snow: Double? = 0.0,

    // Probability of precipitation
    @SerialName("pop")
    var pop: Double = 0.0
) : Serializable
