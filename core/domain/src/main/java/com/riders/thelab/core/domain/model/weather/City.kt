package com.riders.thelab.core.domain.model.weather

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class City(
    val id: Double = 0.0,
    val name: String,
    val state: String? = null,
    val country: String,
    @SerialName("coord")
    val coordinates: Coordinates,
    val uuid: String? = null
) : Serializable
