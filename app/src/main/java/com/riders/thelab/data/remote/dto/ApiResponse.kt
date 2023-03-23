package com.riders.thelab.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val message: String,
    val code: Int,
    val token: String? = null
) {
    constructor() : this("", -1)
}