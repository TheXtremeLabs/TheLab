package com.riders.thelab.core.data.remote.dto.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class City(
    @SerialName("id")
    val id: Double,
    @SerialName("name")
    val name: String,
    @SerialName("state")
    val state: String,
    @SerialName("country")
    val country: String,
    @SerialName("coord")
    val coordinates: Coordinates
) : Serializable