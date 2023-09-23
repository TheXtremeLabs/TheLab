package com.riders.thelab.core.data.local.model

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class SpotifyRequestToken(
    @SerialName(value = "client_id")
    val clientId: String,
    @SerialName(value = "client_secret")
    val clientSecret: String,
    @SerialName(value = "grant_type")
    val grantType: String = "client_credentials"
) : Serializable
