package com.riders.thelab.core.data.local.model.music

import android.net.Uri
import androidx.compose.runtime.Stable
import java.io.File

@Stable
data class SongModel(
    val id: Int,
    val name: String,
    val path: String,
    val drawableUri: String,
    var isPlaying: Boolean
) {
    val uri: Uri = Uri.fromFile(File(path))

    override fun toString(): String =
        "SongModel(id=$id, name='$name', path='$path', drawableUri='$drawableUri', isPlaying=$isPlaying, uri=$uri)"
}