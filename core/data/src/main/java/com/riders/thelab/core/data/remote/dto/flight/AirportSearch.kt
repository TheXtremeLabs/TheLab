package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class AirportSearch(
    @SerialName("city_name")
    val cityName:String,
    @SerialName("airport_code")
    val airportCode:String
): Serializable
