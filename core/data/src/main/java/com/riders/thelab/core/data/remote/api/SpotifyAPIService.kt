package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.spotify.SpotifyResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpotifyAPIService {
    @GET("v1/tracks/{trackID}")
    suspend fun getTrackInfo(
        @Header("Authorization") bearerToken: String,
        @Path("trackID") trackId: String
    ): SpotifyResponse
}