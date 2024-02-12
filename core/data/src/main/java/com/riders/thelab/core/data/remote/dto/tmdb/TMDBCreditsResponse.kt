package com.riders.thelab.core.data.remote.dto.tmdb

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class TMDBCreditsResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "cast")
    val cast: List<TMDBCastDto>,
    @SerialName(value = "crew")
    val crew: List<TMDBCrewDto>
) : Serializable
