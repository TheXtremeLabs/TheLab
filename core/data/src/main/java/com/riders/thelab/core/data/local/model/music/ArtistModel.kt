package com.riders.thelab.core.data.local.model.music

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.artist.Artist
import kotools.types.text.toNotBlankString
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
data class ArtistModel(
    val id: Byte,
    val sceneName: String,
    val firstName: String,
    val secondName: String? = null,
    val lastName: String,
    val dateOfBirth: String,
    val origin: String,
    val debutes: String,
    val activities: String,
    var urlThumb: String,
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
            ""
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
    urlThumb = this.urlThumb,
    description = this.description
)