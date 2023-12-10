package com.riders.thelab.core.data.local.model

import com.riders.thelab.core.data.local.bean.MovieEnum
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Movie(
    @SerialName("category")
    var category: String,
    @SerialName("title")
    var title: String,
    @SerialName("original_title")
    var originalTitle: String,
    @SerialName("synopsis")
    var overview: String,
    @SerialName("genre")
    var genre: String,
    @SerialName("year")
    var year: String,
    @SerialName("url")
    var urlThumbnail: String,
    @SerialName("type")
    var type: String,
    @SerialName("duration")
    var duration: Int,
    @SerialName("director")
    var directors: List<Director>? = null,
    @SerialName("scenario")
    var scenarists: List<Scenarist>? = null,
    @SerialName("casting")
    var cast: List<Actors>? = null,
    @SerialName("popularity")
    var popularity: Int,
    @SerialName("rating")
    var rating: Double
) : Serializable {

    constructor() : this("", "", "", "", "", "", "", "", 0, null, null, null, 0, 0.0)

    /*constructor(title: String?, genre: String?, year: String?, urlThumbnail: String?) : this() {
        this.title = title
        this.genre = genre
        this.year = year
        this.urlThumbnail = urlThumbnail
    }*/

    constructor(movieEnum: MovieEnum) : this(
        category = movieEnum.category.value,
        title = movieEnum.title,
        originalTitle = movieEnum.title,
        overview = movieEnum.overview,
        genre = movieEnum.genre,
        year = movieEnum.year,
        urlThumbnail = movieEnum.url,
        type = movieEnum.url,
        duration = movieEnum.duration,
        popularity = movieEnum.popularity,
        rating = movieEnum.rating
    )

    override fun toString(): String {
        return "Movie(category=$category, title=$title, genre=$genre, year=$year, urlThumbnail=$urlThumbnail, type=$type, overview=$overview, duration=$duration, cast=$cast, rating=$rating)"
    }
}
