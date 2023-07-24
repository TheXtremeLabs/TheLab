package com.riders.thelab.data.remote.dto

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class ApiResponse(
    @SerialName("message")
    val message: String,
    @SerialName("code")
    val code: Int,
    @SerialName("token")
    val token: String? = null
) : Serializable {
    constructor() : this("", -1)
}