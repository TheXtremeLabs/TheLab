package com.riders.thelab.core.data.remote.dto

import kotlinx.serialization.SerialName
import timber.log.Timber
import java.io.Serializable

/**
 * Class use to get spotify token
 */
@kotlinx.serialization.Serializable
data class SpotifyToken(
    @SerialName(value = "access_token")
    val accessToken: String,
    @SerialName(value = "token_type")
    val tokenType: String,
    @SerialName(value = "expires_in")
    val expiresIn: Int
) : Serializable {

    fun createBearerToken(): String {
        Timber.d("createBearerToken()")
        return if (accessToken.isEmpty()) {
            "Error cannot get access token"
        } else {
            "Bearer $accessToken"
        }
    }
}
