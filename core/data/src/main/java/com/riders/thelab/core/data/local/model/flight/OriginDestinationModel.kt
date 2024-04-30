package com.riders.thelab.core.data.local.model.flight

import com.riders.thelab.core.data.remote.dto.flight.OriginDestination
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

fun OriginDestination.toOriginDestinationModel(): OriginDestinationModel = OriginDestinationModel(
    this.codeIcao,
    this.codeIata,
    this.codeLid,
    this.timezone,
    this.name,
    this.city,
    this.airportInfoUrl
)