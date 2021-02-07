package com.riders.thelab.ui.weather;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riders.thelab.core.utils.LabFileManager;
import com.riders.thelab.data.local.LabRepository;
import com.riders.thelab.data.local.model.CitiesEventJson;
import com.riders.thelab.data.local.model.CitiesEventJsonAdapter;
import com.riders.thelab.data.local.model.weather.City;
import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.ui.base.BasePresenterImpl;
import com.riders.thelab.utils.Constants;
import com.riders.thelab.utils.Validator;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class WeatherPresenter extends BasePresenterImpl<WeatherView>
        implements WeatherContract.Presenter {

    @Inject
    WeatherActivity activity;

    @Inject
    LabRepository repository;

    @Inject
    LabService service;

    /* RxJava / RxAndroid */

    // Disposable is used to dispose the subscription when an Observer
    // no longer wants to listen to Observable
    // To prevent memory leak, for example when a task is done
    // and the activity/fragment is already destroyed,
    // the observer try to update the activity/fragment's UI that has been destroyed

    // CompositeDisposable can maintain list of subscriptions in  pool
    // and can dispose them all at once
    // Disposing them in Destroy one bye one is a tedious task and it can be error
    // prone as you might forgot to dispose.
    // In this case we can use CompositeDisposable.
    private final CompositeDisposable compositeDisposable;
    private Observable<List<City>> mCityListObservable;


    private ArrayList<City> cities;
    private Gson mGson;


    @Inject
    WeatherPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getCityData() {

        // First step check si t'as des données dans ta db
        Disposable disposable =
                repository.getAllCities()
                        .subscribe(
                                cityList -> {

                                    if (Validator.isNullOrEmpty(cityList)) {
                                        // Si , non call le ws -> save dans la db
                                        Timber.e("List is empty. No Record found in database");


                                        Timber.e("Call web service...");
                                        // Call web service
                                        getCitiesFromWS()
                                                .subscribe(
                                                        responseBody -> {

                                                            try {

                                                                Timber.d("Unzipped downloaded file...");
                                                                // Step 1 : Unzip
                                                                String unzippedGZipResult = LabFileManager.unzipGzip(responseBody);
                                                                Timber.d("Unzip result : %s", unzippedGZipResult);


                                                                if (Validator.isEmpty(unzippedGZipResult)){
                                                                    Timber.e("Result is empty");
                                                                    return;
                                                                }

                                                                Timber.d("Build Moshi adapter and build object...");
                                                                // Step 2 convert to class object
                                                                Moshi moshi =
                                                                        new Moshi.Builder()
                                                                                .add(new CitiesEventJsonAdapter())
                                                                                .build();
                                                                JsonAdapter<CitiesEventJson> jsonAdapter = moshi.adapter(CitiesEventJson.class);

                                                                List<com.riders.thelab.data.remote.dto.weather.City> cities =
                                                                        jsonAdapter.fromJson(unzippedGZipResult).getCitiesList();

                                                                Timber.d("Save in database...");
                                                                // Step 3 save in database
                                                                repository.insertAllCities(cities)
                                                                        .subscribe(aLong -> {
                                                                            Timber.d("long inserted :%S", aLong);
                                                                        }, throwable -> {
                                                                            Timber.e(throwable);
                                                                        });
                                                            } catch (Exception e) {
                                                                Timber.e(e);
                                                            }

                                                        },
                                                        throwable -> {
                                                            Timber.e("Error while downloading zip file");
                                                            Timber.e(throwable);
                                                        });

                                    } else {
                                        // Si, oui tu recuperes de la db
                                        Timber.d("Record found in database. Continue...");
                                    }
                                }, throwable -> {
                                    Timber.e("Error while fetching records in database");
                                    Timber.e(throwable);
                                });

        compositeDisposable.add(disposable);

    }


    public Single<ResponseBody> getCitiesFromWS() {
        return new Single<ResponseBody>() {
            @Override
            protected void subscribeActual(@NonNull SingleObserver<? super ResponseBody> observer) {
                service.getBulkWeatherCitiesFile()
                        .subscribe(
                                observer::onSuccess,
                                observer::onError
                        );
            }
        }
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void getWeather(String city) {

        getView().showLoader();

        Disposable disposable =
                service.getWeather(city)
                        .subscribe(
                                weatherResponse -> {

                                    if (200 != weatherResponse.getCode()) {
                                        Timber.e("error code : %s", weatherResponse.getCode());
                                    } else {
                                        getView().hideLoader();
                                        getView().updateUI(weatherResponse);
                                    }
                                },
                                Timber::e);

        compositeDisposable.add(disposable);
    }

    @Override
    public void clearDisposables() {

        // don't send events once the activity is destroyed
        compositeDisposable.clear();
    }


    /////////////////////////////////////
    //
    // RX
    //
    /////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Observable<List<City>> getDataFromJson(String fileName) {

        return Observable.fromCallable(() -> {

            mGson = new Gson();
            String json;

            InputStream is = null /*mAssetManager.open(fileName)*/;

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);

            mGson.toJson(json);

            return mGson.fromJson(json, new TypeToken<List<City>>() {
            }.getType());
        });
    }

    public DisposableObserver<List<City>> getCitiesObserver() {

        cities = new ArrayList<>();

        return new DisposableObserver<List<City>>() {
            @Override
            public void onNext(@NotNull List<City> cityList) {

                for (City element : cityList) {

                    if (element.getCountry().equals(Constants.WEATHER_COUNTRY_CODE_FRANCE)
                            || element.getCountry().equals(Constants.WEATHER_COUNTRY_CODE_GUADELOUPE)
                            || element.getCountry().equals(Constants.WEATHER_COUNTRY_CODE_MARTINIQUE)
                            || element.getCountry().equals(Constants.WEATHER_COUNTRY_CODE_GUYANE)
                            || element.getCountry().equals(Constants.WEATHER_COUNTRY_CODE_REUNION))
                        cities.add(element);
                }
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Timber.e("Cannot find file : %s", e.getMessage());

                getView().hideLoader();
                getView().onFetchDataFromFileError(e.getMessage());
            }

            @Override
            public void onComplete() {

                getView().hideLoader();
                getView().onFetchCitySuccessful(cities);
            }
        };
    }

    @SuppressLint("SimpleDateFormat")
    public String formatMillisToTimeHoursMinutesSeconds(long millis) {

        Date date = new Date(millis);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
}
