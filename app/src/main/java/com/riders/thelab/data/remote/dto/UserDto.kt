package com.riders.thelab.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "gender")
    var gender: String,
    @Json(name = "firstName")
    var firstName: String,
    @Json(name = "lastName")
    var lastName: String,
    @Json(name = "email")
    var email: String,
    @Json(name = "password")
    var password: String,
    @Json(name = "phoneNumber")
    var phoneNumber: String,
    @Json(name = "dateOfBirth")
    var dateOfBirth: String,
    @Json(name = "isPremium")
    var isPremium: Boolean = false,
    @Json(name = "isCustomer")
    var isCustomer: Boolean = false,
    @Json(name = "isProvider")
    var isProvider: Boolean = false,
    @Json(name = "token")
    var token: String
) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        false,
        false,
        false,
        ""
    )

    constructor(email: String, password: String) : this(
        "",
        "",
        "",
        email,
        password,
        "",
        "",
        false,
        false,
        false,
        ""
    )

    constructor(
        gender: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phoneNumber: String,
        dateOfBirth: String
    ) : this(
        gender,
        firstName,
        lastName,
        email,
        password,
        phoneNumber,
        dateOfBirth,
        false,
        false,
        false,
        ""
    )
}
