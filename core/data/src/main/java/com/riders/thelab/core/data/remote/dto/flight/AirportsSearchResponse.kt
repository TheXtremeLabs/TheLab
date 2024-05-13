package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class AirportsSearchResponse(
    @SerialName("aList")
    val airports: List<AirportSearch>? = null,
    @SerialName("data")
    val airportsOmniData: List<AirportSearch>? = null
) : Serializable
