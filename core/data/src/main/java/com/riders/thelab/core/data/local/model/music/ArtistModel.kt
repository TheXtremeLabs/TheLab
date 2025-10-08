package com.riders.thelab.core.data.local.model.music

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riders.thelab.core.data.remote.dto.artist.Artist
import kotools.types.text.toNotBlankString
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
@Entity(tableName = "artist")
data class ArtistModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Byte,
    @ColumnInfo(name = "sceneName")
    val sceneName: String,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "secondName")
    val secondName: String? = null,
    @ColumnInfo(name = "lastName")
    val lastName: String,
    @ColumnInfo(name = "dateOfBirth")
    val dateOfBirth: String,
    @ColumnInfo(name = "origin")
    val origin: String,
    @ColumnInfo(name = "debutes")
    val debutes: String,
    @ColumnInfo(name = "activities")
    val activities: String,
    @ColumnInfo(name = "urlThumbnail")
    var urlThumbnail: String? = null,
    @ColumnInfo(name = "thumbnail")
    var encodedThumbnail: String? = null,
    @ColumnInfo(name = "description")
    val description: String,
) : Serializable {
    companion object {
        val mock = ArtistModel(
            1,
            "Pi'erre".toNotBlankString().getOrThrow().toString(),
            "Pi'erre".toNotBlankString().getOrThrow().toString(),
            "",
            "Bourne".toNotBlankString().getOrThrow().toString(),
            "12/06/1990".toNotBlankString().getOrThrow().toString(),
            "Oregon".toNotBlankString().getOrThrow().toString(),
            "",
            "",
            "http://pierrethumb.com".toNotBlankString().getOrThrow().toString(),
            description = ""
        )
    }
}

fun Artist.toModel(index: Byte): ArtistModel = ArtistModel(
    id = index,
    sceneName = this.artistName,
    firstName = this.firstName,
    secondName = this.secondName,
    lastName = this.lastName,
    dateOfBirth = this.dateOfBirth,
    origin = this.origin,
    debutes = this.debutes,
    activities = this.activities,
    urlThumbnail = urlThumb,
    encodedThumbnail = null,
    description = this.description
)