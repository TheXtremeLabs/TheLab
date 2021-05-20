package com.riders.thelab.data.remote.dto.artist

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Artist constructor(
    @Json(name = "artistName")
    var artistName: String,

    @Json(name = "firstName")
    var firstName: String,

    @Json(name = "secondName")
    var secondName: String,

    @Json(name = "lastName")
    var lastName: String,

    @Json(name = "dateOfBirth")
    var dateOfBirth: String,

    @Json(name = "origin")
    var origin: String,

    @Json(name = "debutes")
    var debutes: String,

    @Json(name = "activities")
    var activities: String,

    @Json(name = "urlThumb")
    var urlThumb: String,

    @Json(name = "description")
    var description: String
)
