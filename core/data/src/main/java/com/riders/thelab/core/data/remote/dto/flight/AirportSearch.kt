package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

/**
 * Represents the remote dto response for the onmi airport search
 */
@kotlinx.serialization.Serializable
data class AirportSearch(
    @SerialName("city_name")
    val cityName: String? = null,
    @SerialName("airport_code")
    val airportCode: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("icao")
    val icaoCode: String? = null,
    @SerialName("iata")
    val iataCode: String? = null,
    @SerialName("ops")
    val ops: String? = null
) : Serializable
