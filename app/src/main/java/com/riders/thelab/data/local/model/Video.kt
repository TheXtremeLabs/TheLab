package com.riders.thelab.data.local.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Video constructor(
    @Json(name = "id")
    var id: String,
    @Json(name = "name")
    var name: String,
    @Json(name = "description")
    var description: String,
    @Json(name = "imageUrl")
    var imageUrl: String,
    @Json(name = "videoUrl")
    var videoUrl: String
) : Parcelable
