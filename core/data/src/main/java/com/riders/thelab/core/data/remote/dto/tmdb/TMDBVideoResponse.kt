package com.riders.thelab.core.data.remote.dto.tmdb

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class TMDBVideoResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "results")
    val results: List<VideoDto>
) : Serializable
