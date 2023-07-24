package com.riders.thelab.data.remote.dto

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class UserDto(
    @SerialName("firstName")
    var firstName: String,
    @SerialName("lastName")
    var lastName: String,
    @SerialName("email")
    var email: String,
    @SerialName("password")
    var password: String,
    @SerialName("token")
    var token: String
) : Serializable {
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
