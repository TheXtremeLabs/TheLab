package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json

data class Rain(
    // Rain volume for the last 1 hour, mm
    @Json(name = "1h")
    val lastHour: Double = 0.0,

    // Rain volume for the last 3 hour, mm
    @Json(name = "3h")
    val lastThreeHour: Double = 0.0
)
