package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.flight.Airport
import com.riders.thelab.core.data.remote.dto.flight.AirportsResponse
import com.riders.thelab.core.data.remote.dto.flight.Operator
import com.riders.thelab.core.data.remote.dto.flight.OperatorResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FlightApiService {

    @GET("airports")
    suspend fun getAirports(
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): AirportsResponse

    @GET("airports/{id}")
    suspend fun getAirportById(@Path("id") airportID: String): Airport

    @GET("operators")
    suspend fun getOperators(
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): OperatorResponse

    @GET("operators/{id}")
    suspend fun getOperatorById(@Path("id") operatorID: String): Operator
}