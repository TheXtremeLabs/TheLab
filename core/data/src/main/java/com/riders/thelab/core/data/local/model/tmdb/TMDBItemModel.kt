package com.riders.thelab.core.data.local.model.tmdb

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.Actors
import com.riders.thelab.core.data.local.model.Director
import com.riders.thelab.core.data.local.model.Scenarist
import com.riders.thelab.core.data.remote.dto.tmdb.MovieDto
import com.riders.thelab.core.data.remote.dto.tmdb.TvShowsDto
import com.riders.thelab.core.data.utils.Constants
import java.io.Serializable

@Stable
@kotlinx.serialization.Serializable
data class TMDBItemModel(
    val id: Int,
    val isMovie: Boolean,
    var title: String,
    var originalTitle: String,
    var overview: String,
    val genresID: Set<Int>,
    var year: String,
    val poster: String? = null,
    val backdropPath: String? = null,
    var type: String? = null,
    var duration: Int? = 0,
    val originalLanguage: String,
    var directors: List<Director>? = null,
    var scenarists: List<Scenarist>? = null,
    var cast: List<Actors>? = null,
    val releaseDate: String? = null,
    val firstAirDate: String? = null,
    var popularity: Int,
    var rating: Double
) : Serializable {

    constructor() : this(
        -1,
        false,
        "",
        "",
        "",
        setOf(),
        "",
        "",
        null,
        null,
        null,
        "",
        null,
        null,
        null,
        "",
        "",
        0,
        0.0
    )

    fun getBackdropImageUrl(): String =
        "${Constants.BASE_ENDPOINT_TMDB_IMAGE_W_500}${this.backdropPath}"
    fun getPosterImageUrl(): String =
        "${Constants.BASE_ENDPOINT_TMDB_IMAGE_W_500}${this.poster}"

    companion object {
        val mockItem = MovieDto.mockMovie.toModel()
    }
}

fun MovieDto.toModel(): TMDBItemModel = TMDBItemModel(
    id = this.id,
    isMovie = true,
    title = this.title,
    originalTitle = this.originalTitle,
    originalLanguage = this.originalLanguage,
    overview = this.overview,
    genresID = this.genresID,
    year = this.releaseDate,
    poster = this.poster,
    backdropPath = this.backdropPath,
    rating = this.rating,
    popularity = this.voteNumber
)

fun TvShowsDto.toModel(): TMDBItemModel = TMDBItemModel(
    id = this.id,
    isMovie = true,
    title = this.name,
    originalTitle = this.originalName,
    originalLanguage = this.originalLanguage,
    overview = this.overview,
    genresID = this.genresID,
    year = this.firstAirDate,
    poster = this.poster,
    backdropPath = this.backdropPath,
    rating = this.rating,
    popularity = this.voteNumber
)
