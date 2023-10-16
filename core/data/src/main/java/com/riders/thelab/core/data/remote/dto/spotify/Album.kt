package com.riders.thelab.core.data.remote.dto.spotify

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Album(
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "release_date")
    val releaseDate: String,
    @SerialName(value = "images")
    val images: List<Image>
) : Serializable
