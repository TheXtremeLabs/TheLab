package com.riders.thelab.di

import android.content.res.AssetManager
import com.google.common.net.HttpHeaders
import com.riders.thelab.TheLabApplication
import com.riders.thelab.core.storage.LabFileManager
import com.riders.thelab.data.local.bean.TimeOut
import com.riders.thelab.data.local.model.weather.WeatherKey
import com.riders.thelab.data.remote.api.*
import com.riders.thelab.data.remote.dto.artist.ArtistsResponseJsonAdapter
import com.riders.thelab.utils.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    fun provideOkHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message: String -> Timber.tag("OkHttp").d(message) }
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /* WEATHER */ /* Provide OkHttp for the app */
    @Provides
    @Singleton
    fun provideWeatherOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(TimeOut.TIME_OUT_READ.value.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TimeOut.TIME_OUT_CONNECTION.value.toLong(), TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->

                val original = chain.request()
                val originalHttpUrl = original.url
                var url: HttpUrl? = null
                val json: String?
                var mWeatherKey: WeatherKey? = null


                val mAssetManager: AssetManager =
                    TheLabApplication.getInstance().getContext()
                        .resources
                        .assets

                try {

                    // Get file from assets
                    val `is`: InputStream = mAssetManager
                        .open("weather_api.json")

                    // Read file and store it into json string object
                    json = LabFileManager.tryReadFile(`is`)

                    // Use of Moshi
                    val moshi = Moshi.Builder().build()
                    val jsonAdapter: JsonAdapter<WeatherKey> =
                        moshi.adapter(WeatherKey::class.java)
                    assert(json != null)

                    // Get value from josn string into WeatherKey object
                    mWeatherKey = jsonAdapter.fromJson(json!!)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Timber.e(Objects.requireNonNull(e.message))
                }

                // Request customization: add request headers
                val requestBuilder: Request.Builder

                // Avoid key and metrics when requesting for bulk download
                if (!originalHttpUrl.toString().contains("sample")) {
                    try {

                        if (mWeatherKey != null) {
                            url = originalHttpUrl.newBuilder()
                                .addQueryParameter("appid", mWeatherKey.appID)
                                .addQueryParameter("units", "metric")
                                .build()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Timber.e(Objects.requireNonNull(e.message))
                    }

                    assert(url != null)

                    // Request customization: add request headers
                    requestBuilder = url?.let {
                        original.newBuilder()
                            .url(it)
                            .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                            .header(
                                HttpHeaders.CONNECTION,
                                "close"
                            ) //.header(HttpHeaders.CONTENT_TYPE, "application/json")
                            .header(HttpHeaders.ACCEPT_ENCODING, "Identity")
                    }!!
                } else {
                    url = originalHttpUrl.newBuilder()
                        .build()

                    // Request customization: add request headers
                    requestBuilder = original.newBuilder()
                        .url(url)
                        .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                        .header(HttpHeaders.CONNECTION, "close")
                        .header(HttpHeaders.CACHE_CONTROL, "max-age=60")
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .header(HttpHeaders.ACCEPT_ENCODING, "Identity")
                }
                val request = requestBuilder.build()
                chain.proceed(request)
            })
            .addInterceptor(provideOkHttpLogger())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpArtists(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(TimeOut.TIME_OUT_READ.value.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TimeOut.TIME_OUT_CONNECTION.value.toLong(), TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                // Customize the request
                val request: Request = original.newBuilder()
                    .build()
                val response: Response = chain.proceed(request)
                response.cacheResponse
                response
            })
            .addInterceptor(provideOkHttpLogger())
            .build()
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
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitArtists(url: String): Retrofit {
        val moshi = Moshi.Builder()
            .add(ArtistsResponseJsonAdapter())
            .build()
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttpArtists())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }


    @Provides
    @Singleton
    fun provideWeatherRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideWeatherOkHttp())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @NotNull
    fun provideArtistsAPIService(): ArtistsAPIService {
        return provideRetrofitArtists(Constants.BASE_ENDPOINT_GOOGLE_FIREBASE_API)
            .create(ArtistsAPIService::class.java)
    }


    @Provides
    @Singleton
    @NotNull
    fun provideGoogleAPIService(): GoogleAPIService {
        return provideRetrofit(Constants.BASE_ENDPOINT_GOOGLE_MAPS_API).create(
            GoogleAPIService::class.java
        )
    }


    @Provides
    @Singleton
    @NotNull
    fun provideYoutubeApiService(): YoutubeApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_YOUTUBE)
            .create(YoutubeApiService::class.java)
    }


    @Provides
    @Singleton
    @NotNull
    fun provideWeatherApiService(): WeatherApiService {
        return provideWeatherRetrofit(Constants.BASE_ENDPOINT_WEATHER)
            .create(WeatherApiService::class.java)
    }

    @Provides
    @Singleton
    @NotNull
    fun proWeatherBulkApiService(): WeatherBulkApiService {
        return provideWeatherRetrofit(Constants.BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD)
            .create(WeatherBulkApiService::class.java)
    }

}