package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "1h")
    val speed: Double = 0.0,

    @Json(name = "deg")
    val degree: Int = 0,

    @Json(name = "gust")
    val metrics: String
)
