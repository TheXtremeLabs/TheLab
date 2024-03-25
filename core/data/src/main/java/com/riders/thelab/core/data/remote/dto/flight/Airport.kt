package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Airport(
    @SerialName("code")
    val code: String? = null,
    @SerialName("airport_info_url")
    val airportInfoUrl: String? = null,
    @SerialName("airport_code")
    val airportCode: String? = null,
    @SerialName("alternate_ident")
    val alternateIdent: String? = null,
    @SerialName("code_icao")
    val icaoCode: String? = null,
    @SerialName("code_iata")
    val iataCode: String? = null,
    @SerialName("code_lid")
    val lidCode: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("elevation")
    val elevation: String? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("state")
    val state: String? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("wiki_url")
    val wikiUrl: String? = null,
    @SerialName("airport_flights_url")
    val airportFlightUrl: String? = null,
    @SerialName("alternatives")
    val alternatives: List<Airport>? = null
) : Serializable {
    fun getAirportId(): String {
        return this.airportInfoUrl?.split("/")?.get(1) ?: ""
    }
}