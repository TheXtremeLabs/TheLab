package com.riders.thelab.core.data.local.model.tmdb

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
data class TMDBCrewModel(
    @SerialName("adult")
    val isAdult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
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
    val character: String,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("department")
    val department: String,
    @SerialName("job")
    val job: String
) : Serializable

/*
fun TMDBCrewDto.toModel(): TDMBCrewModel = TDMBCrewModel(
    isAdult,
    gender,
    id,
    department,
    name,
    originalName,
    popularity,
    thumbnail,
    castId,
    character,
    creditId,
    department,
    job
)*/
