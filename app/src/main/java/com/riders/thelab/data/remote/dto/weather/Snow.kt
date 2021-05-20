package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json

data class Snow(
    // Rain volume for the last 1 hour, mm
    @Json(name = "1h")
    var lastHour: Double = 0.0,

    // Rain volume for the last 3 hour, mm
    @Json(name = "3h")
    var lastThreeHour: Double = 0.0
)
