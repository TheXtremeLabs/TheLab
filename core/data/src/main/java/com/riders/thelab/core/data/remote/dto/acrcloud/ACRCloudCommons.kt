package com.riders.thelab.core.data.remote.dto.acrcloud

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Track(
    @SerialName("id") val id: String? = null, @SerialName("name") val name: String
) : Serializable

@kotlinx.serialization.Serializable
data class Artist(
    @SerialName("id") val id: String? = null, @SerialName("name") val name: String
) : Serializable

@kotlinx.serialization.Serializable
data class Album(
    @SerialName("id") val id: String? = null, @SerialName("name") val name: String
) : Serializable

@kotlinx.serialization.Serializable
data class Genre(@SerialName("name") val name: String) : Serializable