package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coordinates(
    @Json(name = "lon")
    val longitude: Double = 0.0,

    @Json(name = "lat")
    val latitude: Double = 0.0
)
