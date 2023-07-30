package com.riders.thelab.core.data.local.model.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class WeatherKey(@SerialName("appid") var appID: String) : Serializable
