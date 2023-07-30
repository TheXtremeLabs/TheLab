package com.riders.thelab.core.data.remote.dto.directions

import com.google.gson.annotations.SerializedName

data class Steps(
    @SerializedName(value = "travel_mode")
    var travelMode: String? = null,

    @SerializedName(value = "start_location")
    var startLocation: Coordinates? = null,

    @SerializedName(value = "end_location")
    var endLocation: Coordinates? = null
)