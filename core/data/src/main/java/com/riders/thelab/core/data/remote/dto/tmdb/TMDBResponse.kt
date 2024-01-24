package com.riders.thelab.core.data.remote.dto.tmdb

import kotlinx.serialization.SerialName

/*
 * i.e. : {"success":false,"status_code":34,"status_message":"The resource you requested could not be found."}
 */
@kotlinx.serialization.Serializable
abstract class TMDBResponse(
    @SerialName(value = "success")
    val success: Boolean = false,
    @SerialName(value = "status_code")
    val code: Int = 0,
    @SerialName(value = "status_message")
    val message: String = ""
)
