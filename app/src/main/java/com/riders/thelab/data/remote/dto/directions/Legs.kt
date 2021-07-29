package com.riders.thelab.data.remote.dto.directions

import com.google.gson.annotations.Expose
import java.util.*

data class Legs(
    @Expose
    var steps: ArrayList<Steps>? = null
)