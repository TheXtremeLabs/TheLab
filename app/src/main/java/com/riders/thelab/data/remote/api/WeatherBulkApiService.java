package com.riders.thelab.data.remote.api;

import com.riders.thelab.utils.Constants;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

public interface WeatherBulkApiService {

    @Streaming
    @GET(Constants.WEATHER_BULK_DOWNLOAD_URL)
    Single<ResponseBody> getCitiesGZipFile();
}