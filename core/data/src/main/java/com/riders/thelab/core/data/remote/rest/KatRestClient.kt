package com.riders.thelab.core.data.remote.rest

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.riders.thelab.core.common.utils.decrypt
import com.riders.thelab.core.data.BuildConfig
import com.riders.thelab.core.data.local.bean.TimeOut
import com.riders.thelab.core.data.remote.api.KatFCMService
import com.riders.thelab.core.data.utils.Constants
import com.riders.thelab.core.data.utils.Headers
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit

class KatRestClient(private val serverKey: String) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private fun provideOkHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message: String -> Timber.tag("OkHttp").d(message) }
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(TimeOut.TIME_OUT_READ.value.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TimeOut.TIME_OUT_CONNECTION.value.toLong(), TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                // Customize the request
                val request: Request = original.newBuilder()
                    .header(Headers.CONTENT_TYPE.value, "application/json; charset=utf-8")
                    .header("Authorization", "key=${BuildConfig.SERVER_API_KEY_FCM.decrypt()}")
                    .build()
                val response: Response = chain.proceed(request)
                response.cacheResponse
                response
            })
            .addInterceptor(provideOkHttpLogger())
            .build()
    }


    private fun provideRetrofit(baseUrl: String): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOkHttp())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    fun getApiService(): KatFCMService {
        return provideRetrofit(Constants.BASE_ENDPOINT_FIREBASE_CLOUD_MESSAGING)
            .create(KatFCMService::class.java)
    }
}