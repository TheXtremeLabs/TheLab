package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class AirportsResponse(
    @SerialName("num_pages")
    val maxPages: Int,
    @SerialName("airports")
    val airports: List<Airport>,
    @SerialName("links")
    val links: Link
) : Serializable
