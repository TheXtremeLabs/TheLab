package com.riders.thelab.core.data.remote.dto.directions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Steps(
    @SerialName(value = "travel_mode")
    var travelMode: String? = null,

    @SerialName(value = "start_location")
    var startLocation: Coordinates? = null,

    @SerialName(value = "end_location")
    var endLocation: Coordinates? = null
) : java.io.Serializable