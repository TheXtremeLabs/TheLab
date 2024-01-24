package com.riders.thelab.core.data.remote.dto.tmdb

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
) : Serializable