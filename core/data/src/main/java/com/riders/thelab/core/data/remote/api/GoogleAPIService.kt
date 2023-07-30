package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.directions.Directions

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleAPIService {
    // Places

    // Places
    // Directions
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String?
    ): Directions
}