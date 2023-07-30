package com.riders.thelab.core.data.remote.dto.weather

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Weather(
    @SerialName("id")
    val id: Int = 0,

    @SerialName("main")
    val main: String,

    @SerialName("description")
    val description: String,

    @SerialName("icon")
    val icon: String
)
