package com.riders.thelab.data.remote.api

import com.riders.thelab.data.remote.dto.directions.Directions
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleAPIService {
    // Places

    // Places
    // Directions
    @GET("directions/json")
    fun getDirections(
            @Query("origin") origin: String,
            @Query("destination") destination: String,
            @Query("key") key: String?): Single<Directions>
}