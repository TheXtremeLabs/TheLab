package com.riders.thelab.data.remote.dto.music

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)
