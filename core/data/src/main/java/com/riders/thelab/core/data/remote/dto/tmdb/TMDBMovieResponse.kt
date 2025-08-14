package com.riders.thelab.core.data.remote.dto.tmdb

import com.riders.thelab.core.data.remote.dto.tmdb.MovieDto.Companion.mockMovie
import com.riders.thelab.core.data.remote.dto.tmdb.MovieDto.Companion.platform2MockMovie
import com.riders.thelab.core.data.remote.dto.tmdb.MovieDto.Companion.venomMockMovie
import com.riders.thelab.core.data.remote.dto.tmdb.MovieDto.Companion.wonkaMockMovie
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
            results = listOf(mockMovie, platform2MockMovie, venomMockMovie, wonkaMockMovie),
            totalResults = 21,
            totalPages = 2
        )
    }
}