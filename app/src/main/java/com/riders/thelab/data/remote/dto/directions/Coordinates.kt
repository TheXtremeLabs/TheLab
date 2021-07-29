package com.riders.thelab.data.remote.dto.directions

import com.google.gson.annotations.SerializedName

class Coordinates(
    @SerializedName("lng")
    var longitude: Double = 0.0,

    @SerializedName("lat")
    var latitude: Double = 0.0,
)