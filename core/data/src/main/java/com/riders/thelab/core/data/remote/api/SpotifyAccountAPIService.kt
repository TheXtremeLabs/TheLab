package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.local.model.SpotifyRequestToken
import com.riders.thelab.core.data.remote.dto.spotify.SpotifyResponse
import com.riders.thelab.core.data.remote.dto.spotify.SpotifyToken
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyAccountAPIService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("token")
    suspend fun getToken(@Body request: SpotifyRequestToken): SpotifyToken

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("token")
    suspend fun getToken(
        @Query("grant_type") grantType: String = "client_credentials",
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
    ): SpotifyToken
}