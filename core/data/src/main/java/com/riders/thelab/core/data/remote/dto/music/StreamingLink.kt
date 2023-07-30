package com.riders.thelab.core.data.remote.dto.music

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class StreamingLink(
    @SerialName("id")
    val id: Int,
    @SerialName("link")
    val link: String,
    @SerialName("platform")
    val platform: String
) : Serializable
