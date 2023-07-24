package com.riders.thelab.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Coordinates(
    @SerialName("lon")
    val longitude: Double = 0.0,

    @SerialName("lat")
    val latitude: Double = 0.0
) : Serializable
