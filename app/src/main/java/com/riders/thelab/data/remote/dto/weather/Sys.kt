package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "type")
    val type: Int = 0,

    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "country")
    val country: String,

    @Json(name = "sunrise")
    val sunrise: Long = 0,

    @Json(name = "sunset")
    val sunset: Long = 0,
)
