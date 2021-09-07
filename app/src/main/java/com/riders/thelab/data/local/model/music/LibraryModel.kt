package com.riders.thelab.data.local.model.music

import android.os.Parcelable
import com.riders.thelab.data.local.bean.ReleaseType
import com.riders.thelab.data.remote.dto.music.Artist
import com.riders.thelab.data.remote.dto.music.Release
import kotlinx.parcelize.Parcelize

@Parcelize
data class LibraryModel(
    var id: Int,
    var releaseName: String,
    var releaseDate: String,
    var releaseType: ReleaseType,
    var releaseCover: String,
    var artists: String,
    var isPlaying: Boolean
) : Parcelable {
    constructor(release: Release, artists: List<Artist>, base64encryptedCover: String) : this(
        0,
        "",
        "",
        ReleaseType.NONE,
        "",
        "",
        false
    ) {
        this.id = release.id
        this.releaseName = release.name
        this.releaseDate = release.releaseDate
        this.releaseType = release.type
        this.releaseCover = base64encryptedCover
        this.artists = getFormattedArtists(artists)
    }

    private fun getFormattedArtists(artists: List<Artist>): String {
        val sb: StringBuilder = StringBuilder()

        if (artists.isEmpty())
            return ""

        return if (1 == artists.size)
            artists[0].name
        else {

            artists.forEach {
                sb.append(it.name).append(", ")
            }

            sb.trim().toString().substring(0, sb.length - 2)
        }
    }
}
