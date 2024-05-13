package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class LastPosition(
    @SerialName("fa_flight_id")
    val faFlightID: NotBlankString? = null,
    @SerialName("altitude")
    val altitude: Int,
    @SerialName("altitude_change")
    val altitudeChange: AltitudeChange,
    @SerialName("groundspeed")
    val groundspeed: Int,
    @SerialName("heading")
    val heading: Int? = null,
    @SerialName("latitude")
    val latitude: Int,
    @SerialName("longitude")
    val longitude: Int,
    @SerialName("timestamp")
    val timestamp: NotBlankString,
    @SerialName("update_type")
    val updateType: UpdateType
) : Serializable
