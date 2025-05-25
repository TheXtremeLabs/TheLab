package com.riders.thelab.core.data.local.model.music

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.riders.thelab.core.data.local.model.Song

@Entity(tableName = "music_recognition")
data class MusicRecognitionModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val _id: Int = 0,
    val genres: Set<String>,
    val title: String,
    val artists: Set<String>,
    val label: String,
    val releaseDate: String,
    val album: String,
    val albumThumbUrl: String
) {
    @Ignore
    constructor(song: Song) : this(
        title = song.title,
        artists = song.artists,
        label = song.label,
        releaseDate = song.releaseDate,
        album = song.album,
        albumThumbUrl = song.albumThumbUrl,
        genres = song.genres,
    )
}
