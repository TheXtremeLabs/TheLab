package com.riders.thelab.data.remote.dto.directions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Directions(
    @Expose
    var route: ArrayList<Route>? = null,

    @Expose
    var status: String? = null,

    @SerializedName("overview_polyline")
    var overviewPolyline: String? = null
)