package com.riders.thelab.data.remote;

import android.location.Location;

import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.data.remote.api.ArtistsAPIService;
import com.riders.thelab.data.remote.api.GoogleAPIService;
import com.riders.thelab.data.remote.api.WeatherApiService;
import com.riders.thelab.data.remote.api.WeatherBulkApiService;
import com.riders.thelab.data.remote.api.YoutubeApiService;
import com.riders.thelab.data.remote.dto.Artist;
import com.riders.thelab.data.remote.dto.directions.Directions;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class LabService {

    private final ArtistsAPIService artistsAPIService;
    private final GoogleAPIService googleAPIService;
    private final YoutubeApiService youtubeApiService;
    private final WeatherApiService weatherApiService;
    private final WeatherBulkApiService weatherBulkApiService;

    @Inject
    public LabService(
            ArtistsAPIService artistsAPIService,
            GoogleAPIService googleAPIService,
            YoutubeApiService youtubeApiService,
            WeatherApiService weatherApiService,
            WeatherBulkApiService weatherBulkApiService) {

        this.artistsAPIService = artistsAPIService;
        this.googleAPIService = googleAPIService;
        this.youtubeApiService = youtubeApiService;
        this.weatherApiService = weatherApiService;
        this.weatherBulkApiService = weatherBulkApiService;
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
    public Single<List<Artist>> getArtists(String url) {
        Timber.e("getArtists()");
        return artistsAPIService
                .getArtists(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Single<Directions> getRoutes(String origin, String destination, String key) {
        Timber.e("getRoutes()");
        return googleAPIService
                .getDirections(
                        origin,
                        destination,
                        key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Video>> getVideos() {
        Timber.e("getVideos()");
        return youtubeApiService
                .fetchYoutubeVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<WeatherResponse> getWeather(final Object object) {
        Timber.e("getWeather()");

        if (null == object) {
            Timber.e("Cannot perform request call object is null");
            return null;
        }

        Single<WeatherResponse> rxSingleWeatherRequest = null;

        if (object instanceof String) {
            rxSingleWeatherRequest =
                    weatherApiService
                            .getCurrentWeatherByCityName((String) object);
        } else if (object instanceof Location) {
            double lat = ((Location) object).getLatitude();
            double lon = ((Location) object).getLongitude();

            rxSingleWeatherRequest =
                    weatherApiService
                            .getCurrentWeatherByGeographicCoordinates(lat, lon);
        }

        return
                rxSingleWeatherRequest == null
                        ? null
                        : rxSingleWeatherRequest
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseBody> getBulkWeatherCitiesFile() {
        Timber.e("get cities bulk file()");
        return weatherBulkApiService
                .getCitiesGZipFile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
