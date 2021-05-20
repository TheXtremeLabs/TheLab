package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json

data class DailyWeather(
    @Json(name = "dt")
    val dateTimeUTC: Long = 0,

    @Json(name = "sunrise")
    val sunrise: Long = 0,

    @Json(name = "sunset")
    val sunset: Long = 0,

    @Json(name = "moonrise")
    val moonrise: Long = 0,

    @Json(name = "moonset")
    val moonset: Long = 0,

    @Json(name = "moon_phase")
    val moonPhase: Double = 0.0,

    @Json(name = "temp")
    val temperature: Temperature,

    @Json(name = "feels_like")
    val feelsLike: FeelsLike,

    @Json(name = "pressure")
    val pressure: Int = 0,

    @Json(name = "humidity")
    val humidity: Int = 0,

    @Json(name = "dew_point")
    val dewPoint: Double = 0.0,

    // UV index
    @Json(name = "uvi")
    val uvIndex: Double = 0.0,

    @Json(name = "clouds")
    val clouds: Int = 0,

    // Average visibility, metres
    @Json(name = "visibility")
    val visibility: Int = 0,

    @Json(name = "wind_speed")
    val windSpeed: Double = 0.0,

    @Json(name = "wind_deg")
    val windDegree: Int = 0,

    @Json(name = "wind_gust")
    val windGust: Double = 0.0,

    @Json(name = "weather")
    val weather: List<Weather>,

    @Json(name = "rain")
    val rain: Double = 0.0,

    @Json(name = "snow")
    val snow: Snow,

    // Probability of precipitation
    @Json(name = "pop")
    var pop: Double = 0.0
)
