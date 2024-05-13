package com.riders.thelab.feature.songplayer.utils

fun String.parseSongName(): String = when {
    this.endsWith(".mp3", true) -> {
        this.split(".mp3")[0]
    }

    this.endsWith(".m4a", true) -> {
        this.split(".m4a")[0]
    }

    else -> {
        ""
    }
}