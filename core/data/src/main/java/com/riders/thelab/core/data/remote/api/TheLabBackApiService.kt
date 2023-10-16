package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.ApiResponse
import com.riders.thelab.core.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TheLabBackApiService {
    @GET("/connect")
    suspend fun getApi(): ApiResponse

    @POST("/login")
    suspend fun login(@Body user: UserDto): ApiResponse

    @POST("/users")
    suspend fun saveUser(@Body user: UserDto): ApiResponse

    /*@PATCH("/users/login")
    suspend fun login(@Body user: User): Unit*/
}