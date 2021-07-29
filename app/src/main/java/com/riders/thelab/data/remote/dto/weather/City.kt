package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "state")
    val state: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "coord")
    val coordinates: Coordinates
) {

    override fun toString(): String {
        return "City(id=$id, name=$name, state=$state, country=$country, coordinates=$coordinates)"
    }
}
