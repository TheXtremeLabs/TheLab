package com.riders.thelab.core.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Sys(
    @SerialName("type")
    val type: Int = 0,

    @SerialName("id")
    val id: Int = 0,

    @SerialName("country")
    val country: String,

    @SerialName("sunrise")
    val sunrise: Long = 0,

    @SerialName("sunset")
    val sunset: Long = 0,
) : Serializable
