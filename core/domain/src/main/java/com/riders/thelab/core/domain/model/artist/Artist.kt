package com.riders.thelab.core.domain.model.artist

data class Artist(
    val artistName: String,
    val firstName: String,
    var secondName: String? = null,
    val lastName: String,
    val dateOfBirth: String,
    var origin: String? = null,
    var debutes: String? = null,
    var activities: String? = null,
    var urlThumb: String,
    var description: String? = null,
    var thumbnailFullUrl: String? = null
) {
    var index: Byte? = null
}