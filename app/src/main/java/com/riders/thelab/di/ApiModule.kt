package com.riders.thelab.di

/*


@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    fun provideOkHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message: String -> Timber.tag("OkHttp").d(message) }
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    */
/* WEATHER *//*
 */
/* Provide OkHttp for the app *//*

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
                val mWeatherKey: WeatherKey? = LabParser.parseJsonFile<WeatherKey>(
                    context = TheLabApplication.getInstance().getContext(),
                    filename = "weather_api.json"
                )

                // Request customization: add request headers
                var requestBuilder: Request.Builder? = null

                // Avoid key and metrics when requesting for bulk download
                if (!originalHttpUrl.toString().contains("sample")) {
                    try {
                        mWeatherKey?.let {
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
                    url?.let {
                        requestBuilder = original.newBuilder()
                            .url(it)
                            .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                            .header(
                                HttpHeaders.CONNECTION,
                                "close"
                            ) //.header(HttpHeaders.CONTENT_TYPE, "application/json")
                            .header(HttpHeaders.ACCEPT_ENCODING, "Identity")

                    }
                } else {
                    url = originalHttpUrl.newBuilder().build()

                    // Request customization: add request headers
                    url?.let {
                        requestBuilder = original.newBuilder()
                            .url(it)
                            .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                            .header(HttpHeaders.CONNECTION, "close")
                            .header(HttpHeaders.CACHE_CONTROL, "max-age=60")
                            .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                            .header(HttpHeaders.ACCEPT_ENCODING, "Identity")
                    }
                }
                val request = requestBuilder?.build()
                chain.proceed(request!!)
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


    */
/* Provide OkHttp for the app *//*

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
            .connectionSpecs(
                listOf(
                    ConnectionSpec.CLEARTEXT,
                    ConnectionSpec.COMPATIBLE_TLS,
                    ConnectionSpec.MODERN_TLS,
                )
            )
            .build()
    }


    */
/* Provide Retrofit for the app *//*

    @Provides
    fun provideRetrofit(url: String): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttp())
            //.addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitArtists(url: String): Retrofit {
        val contentType = "application/json".toMediaType()
        val moshi = Moshi.Builder()
            .add(ArtistsResponseJsonAdapter())
            .build()
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttpArtists())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }


    @Provides
    @Singleton
    fun provideWeatherRetrofit(url: String): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideWeatherOkHttp())
            //.addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
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

    @Provides
    @Singleton
    @NotNull
    fun provideUserAPIService(): TheLabBackApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_THE_LAB_URL)
            .create(TheLabBackApiService::class.java)
    }

}*/
