package com.riders.thelab.data.local.model

import com.riders.thelab.data.local.bean.MovieEnum
import org.parceler.Parcel

@Parcel
data class Movie constructor(
    var title: String? = null,
    var genre: String? = null,
    var year: String? = null,
    var urlThumbnail: String? = null
) {
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