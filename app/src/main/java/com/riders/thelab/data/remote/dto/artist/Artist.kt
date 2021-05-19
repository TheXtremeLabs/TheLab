package com.riders.thelab.data.remote.dto.artist

import com.squareup.moshi.Json

data class Artist constructor(
    @Json(name = "artistName")
    var artistName: String? = null,

    @Json(name = "firstName")
    var firstName: String? = null,

    @Json(name = "secondName")
    var secondName: String? = null,

    @Json(name = "lastName")
    var lastName: String? = null,

    @Json(name = "dateOfBirth")
    var dateOfBirth: String? = null,

    @Json(name = "origin")
    var origin: String? = null,

    @Json(name = "debutes")
    var debutes: String? = null,

    @Json(name = "activities")
    var activities: String? = null,

    @Json(name = "urlThumb")
    var urlThumb: String? = null,

    @Json(name = "description")
    var description: String? = null
)
