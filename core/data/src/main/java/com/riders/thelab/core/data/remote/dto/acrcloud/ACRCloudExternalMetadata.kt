package com.riders.thelab.core.data.remote.dto.acrcloud

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class ExternalMetadata(
    @SerialName("track") val track: Track? = null,
    @SerialName("artist") val artist: Artist? = null,
    @SerialName("album") val album: Album? = null,
    @SerialName("vid") val videoID: String? = null,
) : Serializable

@kotlinx.serialization.Serializable
data class ACRCloudExternalMetadata(
    @SerialName("deezer") val deezer: ExternalMetadata? = null,
    @SerialName("spotify") val spotify: ExternalMetadata? = null,
    @SerialName("youtube") val youtube: ExternalMetadata? = null,
) : Serializable
