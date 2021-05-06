package com.riders.thelab.data.remote;

import android.app.Activity;
import android.location.Location;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
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
     * Google Cloud Auth
     * *********************
     */
    public Single<StorageReference> getStorageReference(Activity activity) {
        return new Single<StorageReference>() {
            @Override
            protected void subscribeActual(@NonNull SingleObserver<? super StorageReference> observer) {
                FirebaseAuth mAuth;
                final FirebaseStorage[] storage = new FirebaseStorage[1];

                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();
                mAuth
                        .signInAnonymously()
                        .addOnCompleteListener(
                                activity,
                                task -> {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Timber.d("signInAnonymously:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        String bucketName = "gs://the-lab-3920e.appspot.com";

                                        storage[0] = FirebaseStorage.getInstance(bucketName);
                                        // Create a storage reference from our app
                                        StorageReference storageRef = storage[0].getReference();
                                        observer.onSuccess(storageRef);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Timber.w("signInAnonymously:failure %s", task.getException().toString());

                                        Toast.makeText(
                                                activity,
                                                "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                        observer.onError(task.getException());
                                    }
                                });
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


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
