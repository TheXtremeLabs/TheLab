package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json

data class Main(
    @Json(name = "temp")
    val temperature: Double = 0.0,

    @Json(name = "feels_like")
    val feelsLike: Double = 0.0,

    @Json(name = "temp_min")
    val minTemperature: Double = 0.0,

    @Json(name = "temp_max")
    val maxTemperature: Double = 0.0,

    @Json(name = "pressure")
    val pressure: Int = 0,

    @Json(name = "humidity")
    val humidity: Int = 0
) {
}