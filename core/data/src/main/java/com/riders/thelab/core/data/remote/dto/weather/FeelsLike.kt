package com.riders.thelab.core.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class FeelsLike(
    @SerialName("day")
    var day: Double = 0.0,
    @SerialName("night")
    val night: Double = 0.0,
    @SerialName("eve")
    val evening: Double = 0.0,
    @SerialName("morn")
    val morning: Double = 0.0
) : Serializable
