package com.riders.thelab.core.data.remote.dto.tmdb

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class VideoDto(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "iso_639_1")
    val iso_639_1: String,
    @SerialName(value = "iso_3166_1")
    val iso_3166_1: String,
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
) : Serializable
