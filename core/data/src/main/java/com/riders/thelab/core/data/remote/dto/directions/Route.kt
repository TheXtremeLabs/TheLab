package com.riders.thelab.core.data.remote.dto.directions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Route(
    @SerialName("legs")
    var legs: ArrayList<Legs>? = null,

    @SerialName("startName")
    var startName: String,

    @SerialName("endName")
    var endName: String,

    @SerialName("startLat")
    var startLat: Double? = null,

    @SerialName("startLng")
    var startLng: Double? = null,

    @SerialName("endLat")
    var endLat: Double? = null,

    @SerialName("endLng")
    var endLng: Double? = null,

    @SerialName("overviewPolyline")
    var overviewPolyline: String? = null
) : java.io.Serializable