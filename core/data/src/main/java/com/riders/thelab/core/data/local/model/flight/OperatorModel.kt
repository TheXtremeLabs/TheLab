package com.riders.thelab.core.data.local.model.flight

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.flight.Operator

@Stable
@Immutable
data class OperatorModel(
    val name: String,
    val shortname: String? = null,
    val city: String? = null,
    val country: String? = null,
    /**
     * The ICAO airport code or location indicator is a four-letter code designating
     * aerodromes around the world.
     */
    val icao: String? = null,
    /**
     * IATA airport code, also known as an IATA location identifier,
     * IATA station code, or simply a location identifier, is a three-letter geocode designating
     * many airports and metropolitan areas around the world
     */
    val iata: String? = null,
    val callsign: String? = null,
    val location: String? = null,
    val phone: String? = null,
    val url: String? = null,
    val wikiUrl: String? = null,
    val alternatives: List<OperatorModel>? = null
)

fun Operator.toModel(): OperatorModel = OperatorModel(
    name = this.name,
    shortname = this.shortname,
    city = this.city,
    country = this.country,
    icao = this.icao,
    iata = this.iata,
    callsign = this.callsign,
    location = this.location,
    phone = this.phone,
    url = this.url,
    wikiUrl = this.wikiUrl,
    alternatives = this.alternatives?.map { it.toModel() }
)
