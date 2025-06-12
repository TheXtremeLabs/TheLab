package com.riders.thelab.core.data.remote.dto.acrcloud

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class ACRCloudMetadataMusic(
    @SerialName("genres")
    val genres: List<Genre>? = null,
    @SerialName("title")
    val title: String,
    @SerialName("artists")
    val artists: List<Artist>,
    @SerialName("album")
    val album: Album,
    @SerialName("label")
    val label: String? = null,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("external_metadata")
    val externalMetadata: ACRCloudExternalMetadata,
) : Serializable

@kotlinx.serialization.Serializable
data class ACRCloudMetadata(
    @SerialName("timestamp_utc")
    val timestampToString: String,
    @SerialName("music")
    val music: List<ACRCloudMetadataMusic>,
) : Serializable
