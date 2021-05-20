package com.riders.thelab.data.local.model.weather

import com.squareup.moshi.Json

data class WeatherKey(@Json(name = "appid") var appID: String? = null)
