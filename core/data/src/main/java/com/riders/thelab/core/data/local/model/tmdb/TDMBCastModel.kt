package com.riders.thelab.core.data.local.model.tmdb

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.tmdb.TDMBCastDto
import kotlinx.serialization.SerialName
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
data class TDMBCastModel(
    @SerialName("adult")
    val isAdult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val department: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val thumbnail: String,
    @SerialName("cast_id")
    val castId: Int,
    @SerialName("character")
    val character: String
) : Serializable {

}

fun TDMBCastDto.toModel(): TDMBCastModel = TDMBCastModel(
    isAdult, gender, id, department, name, originalName, popularity, thumbnail, castId, character
)