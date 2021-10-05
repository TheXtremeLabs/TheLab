package com.riders.thelab.data.local.model

import android.os.Parcelable
import com.riders.thelab.data.local.bean.MovieEnum
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Movie constructor(
    var title: String? = null,
    var genre: String? = null,
    var year: String? = null,
    var urlThumbnail: String? = null
) : Parcelable {
    constructor() : this("", "", "", "") {
    }

    /*constructor(title: String?, genre: String?, year: String?, urlThumbnail: String?) : this() {
        this.title = title
        this.genre = genre
        this.year = year
        this.urlThumbnail = urlThumbnail
    }*/

    constructor(movieEnum: MovieEnum) : this() {
        title = movieEnum.title
        genre = movieEnum.genre
        year = movieEnum.year
        urlThumbnail = movieEnum.url
    }

    override fun toString(): String {
        return "Movie(title=$title, genre=$genre, year=$year, urlThumbnail=$urlThumbnail)"
    }
}
