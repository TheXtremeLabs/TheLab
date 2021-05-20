package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json

data class Coordinates(
    @Json(name = "lon")
    var longitude: Double = 0.0,

    @Json(name = "lat")
    var latitude: Double = 0.0
)
