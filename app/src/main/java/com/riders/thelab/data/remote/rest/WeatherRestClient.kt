package com.riders.thelab.data.remote.rest

import com.riders.thelab.core.okhttp.LabInterceptors.GzipRequestInterceptor
import com.riders.thelab.data.local.bean.TimeOut
import com.riders.thelab.data.remote.api.WeatherBulkApiService
import com.riders.thelab.utils.Constants
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.HttpHeaders
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
                    .header(HttpHeaders.CONNECTION, "close")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=60")
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.ACCEPT_ENCODING, "")
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            })
            .addInterceptor(GzipRequestInterceptor())
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