package com.riders.thelab.core.data.local.model.tmdb

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBCastDto
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBCrewDto
import com.riders.thelab.core.data.utils.Constants
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
    val knownForDepartment: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val profileThumbnail: String? = null,
    @SerialName("cast_id")
    val castId: Int? = null,
    @SerialName("character")
    val character: String? = null,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("order")
    val order: Int = -1,
    @SerialName("department")
    val department: String? = null,
    @SerialName("job")
    val job: String? = null
) : Serializable {

    fun getProfileImageUrl(): String =
        "${Constants.BASE_ENDPOINT_TMDB_IMAGE_W_ORIGINAL}${this.profileThumbnail}"
}

fun TMDBCastDto.toModel(): TDMBCastModel = TDMBCastModel(
    isAdult,
    gender,
    id,
    knownForDepartment,
    name,
    originalName,
    popularity,
    thumbnail,
    castId,
    character,
    creditId,
    order
)


fun TMDBCrewDto.toModel(): TDMBCastModel = TDMBCastModel(
    isAdult,
    gender,
    id,
    knownForDepartment,
    name,
    originalName,
    popularity,
    thumbnail,
    null,
    null,
    creditId,
    -1,
    department,
    job
)