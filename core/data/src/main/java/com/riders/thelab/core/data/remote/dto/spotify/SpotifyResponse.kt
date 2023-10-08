package com.riders.thelab.core.data.remote.dto.spotify

import kotlinx.serialization.SerialName
import java.io.Serializable

/**
 * Class use to get spotify track infp
 */
@kotlinx.serialization.Serializable
data class SpotifyResponse(
    @SerialName(value = "album")
    val album: Album
) : Serializable
