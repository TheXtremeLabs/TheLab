package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json

data class Wind(
    @Json(name = "1h")
    var speed: Double = 0.0,

    @Json(name = "deg")
    var degree: Int = 0,

    @Json(name = "gust")
    var metrics: String
)
