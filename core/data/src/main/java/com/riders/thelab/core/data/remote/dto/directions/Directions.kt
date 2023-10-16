package com.riders.thelab.core.data.remote.dto.directions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Directions(
    @SerialName("route")
    var route: ArrayList<Route>? = null,

    @SerialName("status")
    var status: String? = null,

    @SerialName("overview_polyline")
    var overviewPolyline: String? = null
) : java.io.Serializable