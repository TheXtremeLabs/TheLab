package com.riders.thelab.data.remote.dto.artist

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Artist constructor(
    @SerialName("artistName")
    var artistName: String,

    @SerialName("firstName")
    var firstName: String,

    @SerialName("secondName")
    var secondName: String,

    @SerialName("lastName")
    var lastName: String,

    @SerialName("dateOfBirth")
    var dateOfBirth: String,

    @SerialName("origin")
    var origin: String,

    @SerialName("debutes")
    var debutes: String,

    @SerialName("activities")
    var activities: String,

    @SerialName("urlThumb")
    var urlThumb: String,

    @SerialName("description")
    var description: String
): Serializable {
    var id: Int = 0
}