package com.riders.thelab.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "firstName")
    var firstName: String,
    @Json(name = "lastName")
    var lastName: String,
    @Json(name = "email")
    var email: String,
    @Json(name = "password")
    var password: String,
    @Json(name = "token")
    var token: String
) {
    constructor(firstName: String, lastName: String, email: String, password: String) : this(
        firstName,
        lastName,
        email,
        password,
        ""
    )

    constructor(email: String, password: String, token: String) : this(
        "",
        "",
        email,
        password,
        token
    )
}
