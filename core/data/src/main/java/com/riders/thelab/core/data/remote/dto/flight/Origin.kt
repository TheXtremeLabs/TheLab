package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Origin(
    @SerialName("code_icao")
    val codeIcao: NotBlankString? = null,
    @SerialName("code_iata")
    val codeIata: NotBlankString? = null,
    @SerialName("code_lid")
    val codeLid: NotBlankString? = null,
    @SerialName("timezone")
    val timezone: NotBlankString? = null,
    @SerialName("name")
    val name: NotBlankString? = null,
    @SerialName("city")
    val city: NotBlankString? = null,
    @SerialName("airport_info_url")
    val airportInfoUrl: NotBlankString? = null
) : Serializable
