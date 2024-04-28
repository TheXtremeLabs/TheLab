package com.riders.thelab.core.data.remote.dto.wikimedia

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class WikimediaResponse(
    @SerialName("parse")
    val parsed:Parsed
): Serializable
