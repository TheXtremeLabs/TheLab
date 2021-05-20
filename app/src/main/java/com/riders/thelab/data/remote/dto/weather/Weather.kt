package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json

data class Weather(
    @Json(name = "id")
    private var id: Int = 0,

    @Json(name = "main")
    val main: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "icon")
    val icon: String
)
