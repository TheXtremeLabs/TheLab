package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.kat.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface KatFCMService {

    @POST("fcm/send")
    suspend fun sendNotification(@Body push: PushNotification): Response<ResponseBody>
}