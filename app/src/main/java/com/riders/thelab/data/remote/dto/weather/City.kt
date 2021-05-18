package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json
import lombok.Getter
import lombok.Setter
import lombok.ToString

@Setter
@Getter
@ToString
data class City(
        @Json(name = "id")
        val id: Int,
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "state")
        val state: String? = null,
        @Json(name = "country")
        val country: String? = null,

        @Json(name = "coord")
        val coordinates: Coordinates? = null
) {

    override fun toString(): String {
        return "City(id=$id, name=$name, state=$state, country=$country, coordinates=$coordinates)"
    }
}
