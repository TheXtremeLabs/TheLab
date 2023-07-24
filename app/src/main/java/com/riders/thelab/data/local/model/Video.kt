package com.riders.thelab.data.local.model

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Video constructor(
    @SerialName("id")
    var id: String,
    @SerialName("name")
    var name: String,
    @SerialName("description")
    var description: String,
    @SerialName("imageUrl")
    var imageUrl: String,
    @SerialName("videoUrl")
    var videoUrl: String
) : Serializable
