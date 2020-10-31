package com.riders.thelab.data;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class DataModule {

    private Application application;

    public DataModule(Application application) {
        this.application = application;
        /*database = Room
                .databaseBuilder(application, DaggerDatabase.class, DaggerDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();*/
    }


    /* Provide OkHttp for the app */
    @Provides
    @Singleton
    @NotNull OkHttpClient provideOkHttp(int readTimeOut, long connectTimeOut) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
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
                    }
                })
                .addInterceptor(logging)
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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }



    enum TimeOut {
        TIME_OUT_READ(60),
        TIME_OUT_CONNECTION(60);
        private final int value;

        TimeOut(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}

