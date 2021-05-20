package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json

data class Clouds constructor(
    @Json(name = "all")
    var cloudiness: Int = 0
)
