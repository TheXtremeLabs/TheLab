package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(

    @Json(name = "coord")
    val coordinates: Coordinates,

    @Json(name = "weather")
    val weather: List<Weather>,

    @Json(name = "base")
    val base: String,

    @Json(name = "main")
    val main: Main,

    @Json(name = "visibility")
    val visibility: Int = 0,

    @Json(name = "rain")
    val rain: Rain,

    @Json(name = "wind")
    val wind: Wind,

    @Json(name = "clouds")
    val clouds: Clouds,

    @Json(name = "dt")
    val dt: Long = 0,

    @Json(name = "sys")
    val system: Sys,

    @Json(name = "timezone")
    val timezone: Int = 0,

    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "name")
    val name: String,

    @Json(name = "cod")
    val code: Int = 0,
)
