package com.riders.thelab.data.remote.dto.directions

import com.google.gson.annotations.Expose

data class Legs(
    @Expose
    var steps: ArrayList<Steps>? = null
)