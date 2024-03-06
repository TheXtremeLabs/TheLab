package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Operator(
    @SerialName("code")
    val code: String? = null,
    @SerialName("operator_info_url")
    val operatorInfoUrl: String? = null,
    @SerialName("name")
    val name: String,
    @SerialName("shortname")
    val shortname: String? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("country")
    val country: String? = null,
    /**
     * The ICAO airport code or location indicator is a four-letter code designating
     * aerodromes around the world.
     */
    @SerialName("icao")
    val icao: String? = null,
    /**
     * IATA airport code, also known as an IATA location identifier,
     * IATA station code, or simply a location identifier, is a three-letter geocode designating
     * many airports and metropolitan areas around the world
     */
    @SerialName("iata")
    val iata: String? = null,
    @SerialName("callsign")
    val callsign: String? = null,
    @SerialName("location")
    val location: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("wiki_url")
    val wikiUrl: String? = null,
    @SerialName("alternatives")
    val alternatives: List<Operator>? = null
) : Serializable
