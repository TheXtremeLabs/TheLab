package com.riders.thelab.core.data.remote.dto.music

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Artist(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
) : Serializable
