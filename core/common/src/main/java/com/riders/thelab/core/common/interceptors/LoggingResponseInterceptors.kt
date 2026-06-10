package com.riders.thelab.core.common.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.IOException

class LoggingResponseInterceptors : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        //If we're downloading a file, we dont want to intercept the response
        if (request.body!!.contentType()!!.subtype == "vnd.android.package-archive") return chain.proceed(
            request
        )
        val response: Response = chain.proceed(request)
        val jsonResponse = response.body!!.string()
        Timber.d("Received response : %s", jsonResponse)

        // Re-create the response before returning it because body can be read only once
        @Suppress("DEPRECATION")
        return response
            .newBuilder()
            .body(
                ResponseBody.create(
                    response.body?.contentType(),
                    jsonResponse
                )
            )
            .build()
    }
}