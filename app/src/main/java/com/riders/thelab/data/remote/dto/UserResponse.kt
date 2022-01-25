package com.riders.thelab.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(val code: Int, val message: String, val token: String)
