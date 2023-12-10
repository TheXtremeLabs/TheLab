package com.riders.thelab.core.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Clouds(
    @SerialName("all")
    val cloudiness: Int = 0
) : Serializable
