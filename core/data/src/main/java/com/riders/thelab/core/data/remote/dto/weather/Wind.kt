package com.riders.thelab.core.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Wind(
    @SerialName("1h")
    val speed: Double = 0.0,

    @SerialName("deg")
    val degree: Int = 0,

    @SerialName("gust")
    val metrics: String
) : Serializable
