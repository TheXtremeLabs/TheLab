package com.riders.thelab.data.remote.api

import com.riders.thelab.data.remote.dto.LoginResponse
import com.riders.thelab.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/login")
    suspend fun login(@Body user: UserDto): LoginResponse

    /*@POST("/users")
    suspend fun saveUser(@Body user: UserDto): UserResponse*/

    /*@PATCH("/users/login")
    suspend fun login(@Body user: User): Unit*/
}