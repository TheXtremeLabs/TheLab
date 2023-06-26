package com.riders.thelab.data.local.model

import com.riders.thelab.data.local.bean.MovieEnum
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Movie constructor(
    var category: String? = null,
    var title: String? = null,
    var genre: String? = null,
    var year: String? = null,
    var urlThumbnail: String? = null,
    var type: String? = null,
    var overview: String? = null,
    var duration: Long? = null,
    var cast: List<Actors>? = null,
    var rating: Double? = null
) : Serializable {

    constructor() : this("", "", "", "")

    /*constructor(title: String?, genre: String?, year: String?, urlThumbnail: String?) : this() {
        this.title = title
        this.genre = genre
        this.year = year
        this.urlThumbnail = urlThumbnail
    }*/

    constructor(movieEnum: MovieEnum) : this() {
        category = movieEnum.category.value
        title = movieEnum.title
        genre = movieEnum.genre
        year = movieEnum.year
        urlThumbnail = movieEnum.url
        type = movieEnum.url
        overview = movieEnum.overview
        duration = movieEnum.duration
        rating = movieEnum.rating
    }

    override fun toString(): String {
        return "Movie(category=$category, title=$title, genre=$genre, year=$year, urlThumbnail=$urlThumbnail, type=$type, overview=$overview, duration=$duration, cast=$cast, rating=$rating)"
    }
}
