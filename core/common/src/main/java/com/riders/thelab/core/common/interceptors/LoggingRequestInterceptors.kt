package com.riders.thelab.core.common.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import timber.log.Timber
import java.io.IOException

class LoggingRequestInterceptors : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        //Intercepting the request to log it
        try {
            val buffer = Buffer()
            chain.request().body?.writeTo(buffer)
            Timber.d("Request sent : %s", buffer.readUtf8())
        } catch (ignored: IOException) {
            Timber.e(ignored)
        }
        return chain.proceed(chain.request())
    }
}