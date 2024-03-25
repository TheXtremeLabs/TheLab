package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Destination(
    @SerialName("code_icao")
    val codeIcao: NotBlankString?,
    @SerialName("code_iata")
    val codeIata: NotBlankString?,
    @SerialName("code_lid")
    val codeLid: NotBlankString?,
    @SerialName("timezone")
    val timezone: NotBlankString?,
    @SerialName("name")
    val name: NotBlankString?,
    @SerialName("city")
    val city: NotBlankString?,
    @SerialName("airport_info_url")
    val airportInfoUrl: NotBlankString?
) : Serializable
