package com.riders.thelab.core.data.remote.dto

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class UserResponse(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("token") val token: String
) : Serializable
