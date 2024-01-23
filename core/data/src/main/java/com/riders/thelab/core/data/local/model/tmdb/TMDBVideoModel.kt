package com.riders.thelab.core.data.local.model.tmdb

import com.riders.thelab.core.data.remote.dto.tmdb.VideoDto
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class TMDBVideoModel(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "key")
    val key: String,
    @SerialName(value = "site")
    val site: String,
    @SerialName(value = "size")
    val size: Int,
    @SerialName(value = "type")
    val type: String,
    @SerialName(value = "official")
    val official: Boolean,
    @SerialName(value = "published_at")
    val publishedAt: String
) : Serializable {
    constructor() : this("", "", "", "", 0, "", false, "")
}

fun VideoDto.toModel(): TMDBVideoModel = TMDBVideoModel(
    id, name, key, site, size, type, official, publishedAt
)