package com.riders.thelab.core.data.remote.dto.kat

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class NotificationData(
    @SerialName("title")
    val title: String,
    @SerialName("message")
    val message: String
): Serializable
