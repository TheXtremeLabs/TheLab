package com.riders.thelab.core.data.remote.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginResponse(@SerialName("message") val message: String)
