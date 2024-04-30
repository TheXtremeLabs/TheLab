package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class SearchByRouteResponse(@SerialName("flights") val flights:List<Flight>): Serializable
