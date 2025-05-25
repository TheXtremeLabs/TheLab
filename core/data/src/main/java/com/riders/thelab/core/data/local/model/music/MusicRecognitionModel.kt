package com.riders.thelab.core.data.local.model.music

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riders.thelab.core.data.local.model.Song
import kotlinx.serialization.json.Json

@Entity(tableName = "music_recognition")
data class MusicRecognitionModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val _id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "artists")
    val artists: String,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "releaseDate")
    val releaseDate: String,
    @ColumnInfo(name = "album")
    val album: String,
    @ColumnInfo(name = "albumThumbUrl")
    val albumThumbUrl: String,
    @ColumnInfo(name = "genres")
    val genres: String
)

fun Song.toModel(): MusicRecognitionModel = MusicRecognitionModel(
    title = this.title,
    artists = this.artists.toJson(),
    label = this.label,
    releaseDate = this.releaseDate,
    album = this.album,
    albumThumbUrl = this.albumThumbUrl,
    genres = this.genres.toJson(),
)

private fun Set<*>.toJson() = Json.encodeToString(this)