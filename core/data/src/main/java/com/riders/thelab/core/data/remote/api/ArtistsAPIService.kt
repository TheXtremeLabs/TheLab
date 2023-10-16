package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.artist.Artist


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ArtistsAPIService {
    @GET
    suspend fun getArtists(@Url url: String): List<Artist>

    @GET("{artistsPath}")
    suspend fun getArtistsWithPath(@Path(value = "artistsPath") path: String?): List<Artist>
}