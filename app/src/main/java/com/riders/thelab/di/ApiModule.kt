package com.riders.thelab.di

import com.riders.thelab.data.local.bean.TimeOut
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.HttpHeaders
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    fun provideOkHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message: String -> Timber.tag("OkHttp").d(message) }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    /* Provide OkHttp for the app */
    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(TimeOut.TIME_OUT_READ.value.toLong(), TimeUnit.SECONDS)
                .connectTimeout(TimeOut.TIME_OUT_CONNECTION.value.toLong(), TimeUnit.SECONDS)
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    // Customize the request
                    val request = original.newBuilder()
                            .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                            .header(HttpHeaders.CONNECTION, "close")
                            .header(HttpHeaders.ACCEPT_ENCODING, "Identity")
                            .build()
                    val response = chain.proceed(request)
                    response.cacheResponse
                    response
                })
                .addInterceptor(provideOkHttpLogger())
                .build()
    }


    /* Provide Retrofit for the app */
    @Provides
    fun provideRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttp())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
    }

}