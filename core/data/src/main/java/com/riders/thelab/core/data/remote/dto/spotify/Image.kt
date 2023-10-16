package com.riders.thelab.core.data.remote.dto.spotify

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Image(
    @SerialName(value = "url")
    val url: String,
    @SerialName(value = "width")
    val width: Int,
    @SerialName(value = "height")
    val height: Int,
) : Serializable
