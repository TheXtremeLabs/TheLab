package com.riders.thelab.core.data.remote.dto.tmdb

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class TMDBMovieResponse(
    @SerialName(value = "dates")
    val dates: Dates? = null,
    @SerialName(value = "page")
    val page: Int,
    @SerialName(value = "results")
    val results: List<MovieDto>,
    @SerialName(value = "total_pages")
    val totalPages: Int,
    @SerialName(value = "total_results")
    val totalResults: Int
) : Serializable {
    companion object {
        val mockTMDBMovieResponse = TMDBMovieResponse(
            dates = Dates("2024-02-14", "2024-01-24"),
            page = 1,
            results = listOf(
                MovieDto(
                    adult = false,
                    backdropPath = "/uIk2g2bRkNwNywKZIhC5oIU94Kh.jpg",
                    genresID = setOf(
                        35,
                        10751,
                        14
                    ),
                    id = 787699,
                    originalLanguage = "en",
                    originalTitle = "Wonka",
                    overview = "Willy Wonka – chock-full of ideas and determined to change the world one delectable bite at a time – is proof that the best things in life begin with a dream, and if you’re lucky enough to meet Willy Wonka, anything is possible.",
                    popularity = 1643.733,
                    poster = "/qhb1qOilapbapxWQn9jtRCMwXJF.jpg",
                    releaseDate = "2023-12-06",
                    title = "Wonka",
                    video = false,
                    rating = 7.197,
                    voteNumber = 1274
                )
            ),
            totalResults = 21,
            totalPages = 2
        )
    }
}