package com.riders.thelab.data.remote.rest;

import com.riders.thelab.core.okhttp.LabInterceptors;
import com.riders.thelab.data.local.bean.TimeOut;
import com.riders.thelab.data.remote.api.WeatherBulkApiService;
import com.riders.thelab.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class WeatherRestClient {

    WeatherBulkApiService bulkApiService;

    /**
     * Constructor
     */
    public WeatherRestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD)
                .client(provideOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        bulkApiService = retrofit.create(WeatherBulkApiService.class);
    }

    public WeatherBulkApiService getBulkApiService() {
        return bulkApiService;
    }

    private OkHttpClient provideOkHttpClient() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.readTimeout(TimeOut.TIME_OUT_READ.getValue(), TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(TimeOut.TIME_OUT_CONNECTION.getValue(), TimeUnit.SECONDS);

        httpClientBuilder
                .addInterceptor(chain -> {

                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = null;

                    // Request customization: add request headers
                    Request.Builder requestBuilder;

                    url = originalHttpUrl.newBuilder()
                            .build();

                    // Request customization: add request headers
                    requestBuilder =
                            original.newBuilder()
                                    .url(url)
                                    .header("Connection", "close")
                                    .header("Cache-Control", "max-age=60")
                                    .header("Accept-Ranges", "bytes")
                                    .header("Accept-Encoding", "");

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .addInterceptor(new LabInterceptors.GzipRequestInterceptor())
                .addInterceptor(LabInterceptors.provideLoggingInterceptor());

        //httpClientBuilder.build();
        return httpClientBuilder.build();
    }

}
