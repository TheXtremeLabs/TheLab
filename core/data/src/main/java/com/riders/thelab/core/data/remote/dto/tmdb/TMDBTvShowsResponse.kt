package com.riders.thelab.core.data.remote.dto.tmdb

import com.riders.thelab.core.data.remote.dto.tmdb.TvShowsDto.Companion.mockTvShowsDto
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class TMDBTvShowsResponse(
    @SerialName(value = "dates")
    val dates: Dates? = null,
    @SerialName(value = "page")
    val page: Int,
    @SerialName(value = "results")
    val results: List<TvShowsDto>,
    @SerialName(value = "total_pages")
    val totalPages: Int,
    @SerialName(value = "total_results")
    val totalResults: Int
) : Serializable {
    companion object {
        val mockTMDBTvShowsResponse = TMDBTvShowsResponse(
            dates = Dates("2024-02-14", "2024-01-24"),
            page = 1,
            results = listOf(mockTvShowsDto),
            totalResults = 1,
            totalPages = 2
        )

    }
}