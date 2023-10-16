package com.riders.thelab.core.data.remote.dto.directions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Coordinates(
    @SerialName("longitude")
    var longitude: Double = 0.0,

    @SerialName("lat")
    var latitude: Double = 0.0,
) : java.io.Serializable