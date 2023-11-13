package com.riders.thelab.core.data.remote.rest

import com.riders.thelab.core.common.okhttp.LabInterceptors
import com.riders.thelab.core.data.local.bean.TimeOut
import com.riders.thelab.core.data.remote.api.WeatherBulkApiService
import com.riders.thelab.core.data.utils.Constants
import com.riders.thelab.core.data.utils.Headers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class WeatherRestClient {

    private var bulkApiService: WeatherBulkApiService
    private fun provideOkHttpClient(): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.readTimeout(TimeOut.TIME_OUT_READ.value.toLong(), TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(
            TimeOut.TIME_OUT_CONNECTION.value.toLong(),
            TimeUnit.SECONDS
        )
        httpClientBuilder
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val originalHttpUrl = original.url
                var url: HttpUrl?

                // Request customization: add request headers
                url = originalHttpUrl.newBuilder()
                    .build()

                // Request customization: add request headers
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)
                    .header(Headers.CONNECTION.value, "close")
                    .header(Headers.CACHE_CONTROL.value, "max-age=60")
                    .header(Headers.ACCEPT_RANGES.value, "bytes")
                    .header(Headers.ACCEPT_ENCODING.value, "")
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            })
            .addInterceptor(LabInterceptors.GzipRequestInterceptor())
        //                .addInterceptor(LabInterceptors.provideLoggingInterceptor());

        //httpClientBuilder.build();
        return httpClientBuilder.build()
    }

    /**
     * Constructor
     */
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD)
            .client(provideOkHttpClient())
            .build()
        bulkApiService = retrofit.create(WeatherBulkApiService::class.java)
    }
}