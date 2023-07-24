package com.riders.thelab.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Rain(
    // Rain volume for the last 1 hour, mm
    @SerialName("1h")
    val lastHour: Double = 0.0,

    // Rain volume for the last 3 hour, mm
    @SerialName("3h")
    val lastThreeHour: Double = 0.0
) : Serializable
