package com.riders.thelab.data.remote.dto.music

import com.riders.thelab.data.local.bean.ReleaseType
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Release(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("releaseDate")
    val releaseDate: String,
    @SerialName("type")
    val type: ReleaseType
) : Serializable