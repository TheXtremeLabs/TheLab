package com.riders.thelab.data.local.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Actors(
    val firstname: String,
    val lastName: String,
    val age: Int
) : Serializable
