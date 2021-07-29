package com.riders.thelab.data.local.model.music

data class SongModel(
    val name: String,
    val path: String,
    val drawableUri: String,
    var isPlaying: Boolean
) {
    override fun toString(): String {
        return "SongModel(name='$name', path='$path', drawableUri='$drawableUri')"
    }
}