package com.riders.thelab.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class WeatherResponse(

    @SerialName("coord")
    val coordinates: Coordinates,

    @SerialName("weather")
    val weather: List<Weather>,

    @SerialName("base")
    val base: String,

    @SerialName("main")
    val main: Main,

    @SerialName("visibility")
    val visibility: Int = 0,

    @SerialName("rain")
    val rain: Rain,

    @SerialName("wind")
    val wind: Wind,

    @SerialName("clouds")
    val clouds: Clouds,

    @SerialName("dt")
    val dt: Long = 0,

    @SerialName("sys")
    val system: Sys,

    @SerialName("timezone")
    val timezone: Int = 0,

    @SerialName("id")
    val id: Int = 0,

    @SerialName("name")
    val name: String,

    @SerialName("cod")
    val code: Int = 0,
) : Serializable
