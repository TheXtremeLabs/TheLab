package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.wikimedia.WikimediaResponse
import kotools.types.text.NotBlankString
import retrofit2.http.GET
import retrofit2.http.Query

interface WikimediaApiService {

    @GET("w/api.php?action=parse&format=json&formatversion=2")
    suspend fun getWikimediaResponse(@Query("page") query: NotBlankString): WikimediaResponse
}