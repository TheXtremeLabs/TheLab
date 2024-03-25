package com.riders.thelab.core.data.local.model.flight

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.flight.Airport

@Stable
@Immutable
data class AirportModel(
    val airportId: String? = null,
    val alternateId: String? = null,
    val icaoCode: String? = null,
    val iataCode: String? = null,
    val lidCode: String? = null,
    val name: String? = null,
    val type: String? = null,
    val elevation: String? = null,
    val city: String? = null,
    val state: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val timezone: String? = null,
    val wikiUrl: String? = null,
    val airportFlightUrl: String? = null,
)

fun Airport.toModel(): AirportModel = AirportModel(
    airportId = this.airportCode ?: this.getAirportId(),
    alternateId = this.alternateIdent,
    icaoCode = this.icaoCode,
    iataCode = this.iataCode,
    lidCode = this.lidCode,
    name = this.name,
    type = this.type,
    elevation = this.elevation,
    city = this.city,
    state = this.state,
    longitude = this.longitude,
    latitude = this.latitude,
    timezone = this.timezone,
    wikiUrl = this.wikiUrl,
    airportFlightUrl = this.airportFlightUrl
)




