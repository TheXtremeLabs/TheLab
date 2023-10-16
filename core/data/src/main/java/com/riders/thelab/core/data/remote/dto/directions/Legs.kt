package com.riders.thelab.core.data.remote.dto.directions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Legs(
    @SerialName("steps")
    var steps: ArrayList<Steps>? = null
) : java.io.Serializable