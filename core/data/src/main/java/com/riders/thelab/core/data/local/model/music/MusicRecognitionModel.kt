package com.riders.thelab.core.data.local.model.music

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.remote.dto.acrcloud.ACRCloudResponse
import kotlinx.serialization.json.Json
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi
import timber.log.Timber

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
    var albumThumbUrl: String? = null,
    @ColumnInfo(name = "albumThumbBase64")
    var albumThumbBase64: String? = null,
    @ColumnInfo(name = "genres")
    val genres: String,
    @ColumnInfo(name = "spotifyTrackId")
    var spotifyTrackId: String? = null,
)

/////////////////////////////////////////////////////////
//
// EXTENSIONS
//
/////////////////////////////////////////////////////////
private val json = Json {
    isLenient = true
}

@OptIn(ExperimentalKotoolsTypesApi::class)
private inline fun <reified T> Set<T>.toJson(): String = runCatching {
    json.encodeToString(this)
        .removePrefix("[")
        .removePrefix("\"")
        .removeSuffix("\"")
        .removeSuffix("]")
}
    .onFailure { throwable ->
        throwable.printStackTrace()
        Timber.e("Set<T>.toJson() | onFailure | error caught with message: ${throwable.message} (class: ${throwable.javaClass.canonicalName})")
    }
    .getOrElse { "N/A".toNotBlankString().toString() }


fun Song.toModel(): MusicRecognitionModel = MusicRecognitionModel(
    title = this.title,
    artists = this.artists.joinToString(", "),
    label = this.label,
    releaseDate = this.releaseDate,
    album = this.album,
    albumThumbUrl = this.albumThumbUrl,
    genres = this.genres.toJson(),
    spotifyTrackId = this.externalMetadata["trackID"].toString()
)

@OptIn(ExperimentalKotoolsTypesApi::class)
fun ACRCloudResponse.toModel(): MusicRecognitionModel {
    val title = this.metadata?.music?.get(0)?.externalMetadata?.spotify?.track?.name
        ?: this.metadata?.music?.get(0)?.externalMetadata?.deezer?.track?.name
        ?: this.metadata?.music?.get(0)?.title
        ?: "N/A".toNotBlankString().toString()

    val artists = this.metadata?.music?.get(0)?.externalMetadata?.spotify?.artist?.name
        ?: this.metadata?.music?.get(0)?.externalMetadata?.deezer?.artist?.name
        ?: this.metadata?.music?.get(0)?.artists?.joinToString(", ") { it.name }
        ?: "N/A".toNotBlankString().toString()

    val album = this.metadata?.music?.get(0)?.externalMetadata?.spotify?.album?.name
        ?: this.metadata?.music?.get(0)?.externalMetadata?.deezer?.album?.name
        ?: this.metadata?.music?.get(0)?.album?.name
        ?: "N/A".toNotBlankString().toString()

    return MusicRecognitionModel(
        title = title,
        artists = artists,
        album = album,
        label = this.metadata?.music?.get(0)?.label ?: "N/A".toNotBlankString().toString(),
        releaseDate = this.metadata?.music?.get(0)?.releaseDate
            ?: "N/A".toNotBlankString().toString(),
        genres = this.metadata?.music?.get(0)?.genres?.joinToString(", ") { it.name }
            ?: "N/A".toNotBlankString().toString()
    )
}