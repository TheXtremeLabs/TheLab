package com.riders.thelab.data.local.model.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherKey(@Json(name = "appid") var appID: String)
