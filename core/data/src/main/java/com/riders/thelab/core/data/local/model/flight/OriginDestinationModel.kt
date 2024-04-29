package com.riders.thelab.core.data.local.model.flight

import com.riders.thelab.core.data.remote.dto.flight.Destination
import com.riders.thelab.core.data.remote.dto.flight.Origin
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class OriginDestinationModel(
    val codeIcao: NotBlankString?,
    val codeIata: NotBlankString?,
    val codeLid: NotBlankString?,
    val timezone: NotBlankString?,
    val name: NotBlankString?,
    val city: NotBlankString?,
    val airportInfoUrl: NotBlankString?
) : Serializable

fun Origin.toOriginDestinationModel(): OriginDestinationModel = OriginDestinationModel(
    this.codeIcao,
    this.codeIata,
    this.codeLid,
    this.timezone,
    this.name,
    this.city,
    this.airportInfoUrl
)

fun Destination.toOriginDestinationModel(): OriginDestinationModel = OriginDestinationModel(
    this.codeIcao,
    this.codeIata,
    this.codeLid,
    this.timezone,
    this.name,
    this.city,
    this.airportInfoUrl
)
