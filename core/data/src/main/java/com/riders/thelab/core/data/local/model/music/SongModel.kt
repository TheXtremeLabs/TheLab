package com.riders.thelab.core.data.local.model.music

import androidx.compose.runtime.Stable

@Stable
data class SongModel(
    val id: Int,
    val name: String,
    val path: String,
    val drawableUri: String,
    var isPlaying: Boolean
)