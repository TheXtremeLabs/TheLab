package com.riders.thelab.data;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.AssetManager;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.riders.thelab.TheLabApplication;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.LabDatabase;
import com.riders.thelab.data.local.LabRepository;
import com.riders.thelab.data.local.bean.TimeOut;
import com.riders.thelab.data.local.dao.ContactDao;
import com.riders.thelab.data.local.dao.WeatherDao;
import com.riders.thelab.data.local.model.weather.WeatherKey;
import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.data.remote.api.GoogleAPIService;
import com.riders.thelab.data.remote.api.WeatherApiService;
import com.riders.thelab.data.remote.api.WeatherBulkApiService;
import com.riders.thelab.data.remote.api.YoutubeApiService;
import com.riders.thelab.utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class DataModule {

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


    /*  GENERAL */
    @Provides
    @Singleton
    @NotNull Gson provideGsonFactory() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .setLenient()
                .create();
    }


    @Provides
    @Singleton
    @NotNull HttpLoggingInterceptor provideOkHttpLogger() {
        return new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message))
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    /* Provide OkHttp for the app */
    @Provides
    @Singleton
    @NotNull OkHttpClient provideOkHttp(int readTimeOut, long connectTimeOut) {

        return new OkHttpClient.Builder()
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    // Customize the request
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json; charset=utf-8")
                            .header("Connection", "close")
                            //.header("Content-Type", "application/json")
                            .header("Accept-Encoding", "Identity")
                            .build();

                    Response response = chain.proceed(request);
                    response.cacheResponse();
                    // Customize or return the response
                    return response;
                })
                .addInterceptor(provideOkHttpLogger())
                .build();
    }


    /* Provide Retrofit for the app */
    @Provides
    @Singleton
    @NotNull Retrofit provideRetrofit(String url) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttp(
                        TimeOut.TIME_OUT_READ.getValue(),
                        TimeOut.TIME_OUT_CONNECTION.getValue()
                ))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }


    /* WEATHER */
    /* Provide OkHttp for the app */
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

                    WeatherKey model;

                    JSONObject obj = new JSONObject();

                    AssetManager mAssetManager =
                            TheLabApplication.getContext()
                                    .getResources()
                                    .getAssets();

                    try {

                        InputStream is = mAssetManager
                                .open("weather_api.json");

                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();

                        if (LabCompatibilityManager.isKitkat()) {
                            json = new String(buffer, StandardCharsets.UTF_8);
                            obj = new JSONObject(json);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Timber.e(Objects.requireNonNull(e.getMessage()));
                    }

                    // Request customization: add request headers
                    Request.Builder requestBuilder;

                    // Avoid key and metrics when requesting for bulk download
                    if (!originalHttpUrl.toString().contains("sample")) {
                        try {
                            url = originalHttpUrl.newBuilder()
                                    .addQueryParameter("appid", (String) obj.get("appid"))
                                    .addQueryParameter("units", "metric")
                                    .build();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Timber.e(Objects.requireNonNull(e.getMessage()));
                        }

                        // Request customization: add request headers
                        requestBuilder =
                                original.newBuilder()
                                        .url(url)
                                        .header("Content-Type", "application/json; charset=utf-8")
                                        .header("Connection", "close")
                                        //.header("Content-Type", "application/json")
                                        .header("Accept-Encoding", "Identity");
                    } else {
                        url = originalHttpUrl.newBuilder()
                                .build();

                        // Request customization: add request headers
                        requestBuilder =
                                original.newBuilder()
                                        .url(url)
                                        .header("Content-Type", "text/plain")
                                        .header("Connection", "close")
                                        .header("Cache-Control", "max-age=60")
                                        .header("Accept-Ranges", "bytes")
                                        .header("Accept-Encoding", "Identity");
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
                .addConverterFactory(GsonConverterFactory.create(provideGsonFactory()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
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
                provideGoogleAPIService(),
                provideYoutubeApiService(),
                provideWeatherApiService(),
                proWeatherBulkApiService());
    }

}

