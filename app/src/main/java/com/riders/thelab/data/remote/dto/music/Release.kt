package com.riders.thelab.data.remote.dto.music

import com.riders.thelab.data.local.bean.ReleaseType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Release(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "releaseDate")
    val releaseDate: String,
    @Json(name = "type")
    val type: ReleaseType
)