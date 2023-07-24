package com.riders.thelab.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Main(
    @SerialName("temp")
    val temperature: Double = 0.0,

    @SerialName("feels_like")
    val feelsLike: Double = 0.0,

    @SerialName("temp_min")
    val minTemperature: Double = 0.0,

    @SerialName("temp_max")
    val maxTemperature: Double = 0.0,

    @SerialName("pressure")
    val pressure: Int = 0,

    @SerialName("humidity")
    val humidity: Int = 0
) : Serializable