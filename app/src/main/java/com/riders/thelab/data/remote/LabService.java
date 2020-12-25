package com.riders.thelab.data.remote;

import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.data.remote.api.WeatherApiService;
import com.riders.thelab.data.remote.api.YoutubeApiService;
import com.riders.thelab.data.remote.dto.weather.Weather;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class LabService {


    YoutubeApiService youtubeApiService;
    WeatherApiService weatherApiService;

    @Inject
    public LabService(YoutubeApiService youtubeApiService, WeatherApiService weatherApiService) {
        Timber.d("LabService()");
        this.youtubeApiService = youtubeApiService;
        this.weatherApiService = weatherApiService;
    }


    /////////////////////////////////////
    //
    // REST API
    //
    /////////////////////////////////////

    /**
     * **********************
     * GET
     * *********************
     */
    public Single<List<Video>> getVideos() {
        Timber.e("getVideos()");
        return youtubeApiService
                .fetchYoutubeVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<WeatherResponse> getWeather(String city) {
        Timber.e("getWeather()");
        return weatherApiService
                .getCurrentWeatherByCityName(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<Weather> getWeather(int lat, int lon) {
        Timber.e("getWeather()");
        return weatherApiService
                .getCurrentWeatherByGeographicCoordinates(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
