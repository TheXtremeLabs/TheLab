package com.riders.thelab.core.data.remote.dto.tmdb

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class MovieDto(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "title")
    val title: String,
    @SerialName(value = "adult")
    val adult: Boolean,
    @SerialName(value = "backdrop_path")
    val backdropPath: String,
    @SerialName(value = "genre_ids")
    val genresID: Set<Int>,
    @SerialName(value = "original_language")
    val originalLanguage: String,
    @SerialName(value = "original_title")
    val originalTitle: String,
    @SerialName(value = "overview")
    val overview: String,
    @SerialName(value = "popularity")
    val popularity: Double,
    @SerialName(value = "poster_path")
    val poster: String,
    @SerialName(value = "release_date")
    val releaseDate: String,
    @SerialName(value = "video")
    val video: Boolean,
    @SerialName(value = "vote_average")
    val rating: Double,
    @SerialName(value = "vote_count")
    val voteNumber: Int,
) : Serializable {

    constructor() : this(0, "", false, "", emptySet(), "", "", "", 0.0, "", "", false, 0.0, 0)

    companion object {
        fun getMockMovie(): MovieDto = MovieDto(
            0,
            "",
            false,
            "/efpojdpcjzidcjpzdko.jpg",
            emptySet(),
            "en-US",
            "Expend4bles",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
            50.6,
            "/fv45onsdvdv.jpg",
            "2023-10-25",
            false,
            50.56,
            3455
        )
    }
}