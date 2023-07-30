package com.riders.thelab.core.data.remote.dto.directions

import com.google.gson.annotations.Expose

data class Legs(
    @Expose
    var steps: ArrayList<Steps>? = null
)