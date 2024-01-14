package com.riders.thelab.core.data.remote.dto.tmdb

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Dates(
    val maximum: String,
    val minimum: String
) : Serializable
