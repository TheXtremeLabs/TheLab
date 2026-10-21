package com.riders.thelab.feature.artists.data.local.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
data class ArtistUi(
    val index: Byte? = null,
    val artistName: String,
    val firstName: String,
    var secondName: String? = null,
    val lastName: String,
    val dateOfBirth: String,
    val origin: String? = null,
    val debutes: String? = null,
    val activities: String? = null,
    val urlThumb: String,
    val description: String? = null,
    val thumbnailFullUrl: String? = null
) : Serializable
