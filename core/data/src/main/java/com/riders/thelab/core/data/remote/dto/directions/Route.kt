package com.riders.thelab.core.data.remote.dto.directions

import com.google.gson.annotations.Expose

data class Route(
    @Expose
    var legs: ArrayList<Legs>? = null,

    @Expose
    var startName: String,

    @Expose
    var endName: String,

    @Expose
    var startLat: Double? = null,

    @Expose
    var startLng: Double? = null,

    @Expose
    var endLat: Double? = null,

    @Expose
    var endLng: Double? = null,

    @Expose
    var overviewPolyline: String? = null
)