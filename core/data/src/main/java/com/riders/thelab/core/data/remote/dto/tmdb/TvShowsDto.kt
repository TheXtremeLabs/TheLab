package com.riders.thelab.core.data.remote.dto.tmdb

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class TvShowsDto(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "backdrop_path")
    val backdropPath: String?,
    @SerialName(value = "genre_ids")
    val genresID: Set<Int>,
    @SerialName(value = "original_language")
    val originalLanguage: String,
    @SerialName(value = "original_name")
    val originalName: String,
    @SerialName(value = "overview")
    val overview: String,
    @SerialName(value = "popularity")
    val popularity: Double,
    @SerialName(value = "poster_path")
    val poster: String?,
    @SerialName(value = "first_air_date")
    val firstAirDate: String,
    @SerialName(value = "vote_average")
    val rating: Double,
    @SerialName(value = "vote_count")
    val voteNumber: Int,
) : Serializable {
    companion object {
        fun getMockMovie(): TvShowsDto = TvShowsDto(
            0,
            "",
            "/efpojdpcjzidcjpzdko.jpg",
            emptySet(),
            "en-US",
            "Expend4bles",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
            50.6,
            "/fv45onsdvdv.jpg",
            "2023-10-25",
            50.56,
            3455
        )
    }
}