package com.riders.thelab.core.data.local.model.youtube

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.youtube.VideoDto
import java.io.Serializable

@Immutable
@Stable
data class Video(
    var id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String
) : Serializable {
    constructor() : this("", "", "", "", "")
}


fun VideoDto.toModel(): Video = Video(id, name, description, imageUrl, videoUrl)