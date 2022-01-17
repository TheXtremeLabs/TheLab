package com.riders.thelab.data.remote.dto.weather

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Coordinates(
    @Json(name = "lon")
    val longitude: Double = 0.0,

    @Json(name = "lat")
    val latitude: Double = 0.0
): Parcelable
