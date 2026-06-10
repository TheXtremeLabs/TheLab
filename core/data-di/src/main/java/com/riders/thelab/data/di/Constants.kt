package com.riders.thelab.data.di

import kotlinx.serialization.json.Json

object Constants {

    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
        explicitNulls = false
    }

    const val CONTENT_TYPE_JSON: String = "application/json; charset=utf-8"
}