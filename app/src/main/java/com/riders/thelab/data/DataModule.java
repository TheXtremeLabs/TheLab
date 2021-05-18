package com.riders.thelab.data;

//import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

//@Module
public class DataModule {
/*

    private static DataModule mInstance;
    private final Application application;
    private final LabDatabase database;

    public DataModule(Application application) {
        this.application = application;
        database = Room
                .databaseBuilder(
                        application,
                        LabDatabase.class,
                        LabDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        mInstance = this;
    }


    public static DataModule getInstance() {
        return mInstance;
    }

    public LabDatabase getDatabase() {
        return database;
    }


    @Provides
    @Singleton
    ContactDao providesContactDao() {
        return database.getContactDao();
    }

    @Provides
    @Singleton
    WeatherDao providesWeatherDao() {
        return database.getWeatherDao();
    }


    @Provides
    @Singleton
    LabRepository providesLabRepository() {
        return new LabRepository(providesContactDao(), providesWeatherDao());
    }


    */
    /*  GENERAL *//*

    @Provides
    @Singleton
    @NotNull HttpLoggingInterceptor provideOkHttpLogger() {
        return new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message))
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    */
    /* Provide OkHttp for the app *//*

    @Provides
    @Singleton
    @NotNull OkHttpClient provideOkHttp() {

        return new OkHttpClient.Builder()
                .readTimeout(TimeOut.TIME_OUT_READ.getValue(), TimeUnit.SECONDS)
                .connectTimeout(TimeOut.TIME_OUT_CONNECTION.getValue(), TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    // Customize the request
                    Request request = original.newBuilder()
                            .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                            .header(HttpHeaders.CONNECTION, "close")
                            .header(HttpHeaders.ACCEPT_ENCODING, "Identity")
                            .build();

                    Response response = chain.proceed(request);
                    response.cacheResponse();
                    // Customize or return the response
                    return response;
                })
                .addInterceptor(provideOkHttpLogger())
                .build();
    }


    */
    /* Provide Retrofit for the app *//*

    @Provides
    @Singleton
    @NotNull Retrofit provideRetrofit(String url) {

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttp())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @NotNull Retrofit provideRetrofitArtists(String url) {

        Moshi moshi =
                new Moshi.Builder()
                        .add(new ArtistsResponseJsonAdapter())
                        .build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpArtists())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
    }


    @Provides
    @Singleton
    @NotNull OkHttpClient provideOkHttpArtists() {
        return new OkHttpClient.Builder()
                .readTimeout(TimeOut.TIME_OUT_READ.getValue(), TimeUnit.SECONDS)
                .connectTimeout(TimeOut.TIME_OUT_CONNECTION.getValue(), TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    // Customize the request
                    Request request = original.newBuilder()
                            .build();

                    Response response = chain.proceed(request);
                    response.cacheResponse();
                    // Customize or return the response
                    return response;
                })
                .addInterceptor(provideOkHttpLogger())
                .build();
    }


    */
    /* WEATHER *//*

     */
    /* Provide OkHttp for the app *//*

    @SuppressLint("NewApi")
    @Provides
    @Singleton
    @NotNull OkHttpClient provideWeatherOkHttp(int readTimeOut, long connectTimeOut) {

        return new OkHttpClient.Builder()
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .addInterceptor(chain -> {

                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = null;
                    String json;

                    WeatherKey mWeatherKey = null;

//                    JSONObject obj = new JSONObject();

                    AssetManager mAssetManager =
                            TheLabApplication.getContext()
                                    .getResources()
                                    .getAssets();

                    try {

                        // Get file from assets
                        InputStream is = mAssetManager
                                .open("weather_api.json");

                        // Read file and store it into json string object
                        json = LabFileManager.tryReadFile(is);

                        // Use of Moshi
                        Moshi moshi = new Moshi.Builder().build();
                        JsonAdapter<WeatherKey> jsonAdapter = moshi.adapter(WeatherKey.class);

                        assert json != null;
                        // Get value from josn string into WeatherKey object
                        mWeatherKey = jsonAdapter.fromJson(json);

                        */
/*int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();

                        if (LabCompatibilityManager.isKitkat()) {
                            json = new String(buffer, StandardCharsets.UTF_8);
                            obj = new JSONObject(json);
                        }*//*


                    } catch (Exception e) {
                        e.printStackTrace();
                        Timber.e(Objects.requireNonNull(e.getMessage()));
                    }

                    // Request customization: add request headers
                    Request.Builder requestBuilder;

                    // Avoid key and metrics when requesting for bulk download
                    if (!originalHttpUrl.toString().contains("sample")) {
                        try {
                            assert mWeatherKey != null;
                            url = originalHttpUrl.newBuilder()
                                    .addQueryParameter("appid", mWeatherKey.getAppID())
                                    .addQueryParameter("units", "metric")
                                    .build();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Timber.e(Objects.requireNonNull(e.getMessage()));
                        }

                        assert url != null;
                        // Request customization: add request headers
                        requestBuilder =
                                original.newBuilder()
                                        .url(url)
                                        .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                                        .header(HttpHeaders.CONNECTION, "close")
                                        //.header(HttpHeaders.CONTENT_TYPE, "application/json")
                                        .header(HttpHeaders.ACCEPT_ENCODING, "Identity");
                    } else {
                        url = originalHttpUrl.newBuilder()
                                .build();

                        // Request customization: add request headers
                        requestBuilder =
                                original.newBuilder()
                                        .url(url)
                                        .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                                        .header(HttpHeaders.CONNECTION, "close")
                                        .header(HttpHeaders.CACHE_CONTROL, "max-age=60")
                                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                                        .header(HttpHeaders.ACCEPT_ENCODING, "Identity");
                    }

                    Request request = requestBuilder.build();

                    return chain.proceed(request);
                })
                .addInterceptor(provideOkHttpLogger())
                .build();
    }

    @Provides
    @Singleton
    @NotNull Retrofit provideWeatherRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideWeatherOkHttp(
                        TimeOut.TIME_OUT_READ.getValue(),
                        TimeOut.TIME_OUT_CONNECTION.getValue()
                ))
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @NotNull ArtistsAPIService provideArtistsAPIService() {
        return provideRetrofitArtists(Constants.BASE_ENDPOINT_GOOGLE_FIREBASE_API)
                .create(ArtistsAPIService.class);
    }


    @Provides
    @Singleton
    @NotNull GoogleAPIService provideGoogleAPIService() {
        return provideRetrofit(Constants.BASE_ENDPOINT_GOOGLE_MAPS_API).create(GoogleAPIService.class);
    }


    @Provides
    @Singleton
    @NotNull YoutubeApiService provideYoutubeApiService() {
        return provideRetrofit(Constants.BASE_ENDPOINT_YOUTUBE)
                .create(YoutubeApiService.class);
    }


    @Provides
    @Singleton
    @NotNull WeatherApiService provideWeatherApiService() {
        return provideWeatherRetrofit(Constants.BASE_ENDPOINT_WEATHER)
                .create(WeatherApiService.class);
    }

    @Provides
    @Singleton
    @NotNull WeatherBulkApiService proWeatherBulkApiService() {
        return provideWeatherRetrofit(Constants.BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD)
                .create(WeatherBulkApiService.class);
    }


    @Provides
    @Singleton
    LabService providesLabService() {
        return new LabService(
                provideArtistsAPIService(),
                provideGoogleAPIService(),
                provideYoutubeApiService(),
                provideWeatherApiService(),
                proWeatherBulkApiService());
    }
*/

}

