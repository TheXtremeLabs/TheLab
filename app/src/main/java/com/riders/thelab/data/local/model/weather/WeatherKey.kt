package com.riders.ktweather.data.local.model

import com.squareup.moshi.Json
import lombok.Getter
import lombok.Setter
import lombok.ToString

@Setter
@Getter
@ToString
data class WeatherKey(@Json(name = "appid") var appID: String? = null)
