package com.riders.thelab.ui.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.riders.thelab.TheLabApplication;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.model.weather.City;
import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.ui.base.BasePresenterImpl;
import com.riders.thelab.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class WeatherPresenter extends BasePresenterImpl<WeatherView>
        implements WeatherContract.Presenter {

    @Inject
    WeatherActivity activity;

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
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<List<City>> mCityListObservable;

    private final AssetManager mAssetManager;

    private ArrayList<City> cities;
    private Gson mGson;

    @Inject
    LabService service;


    @Inject
    WeatherPresenter() {
        mAssetManager = TheLabApplication.getContext().getAssets();
    }

    @Override
    public void getCityDataFromFile() {

        // Check sdk version
        if (LabCompatibilityManager.isMarshmallow()) {

            Timber.d("device's sdk version is above 6.0+");

            //Verify permission for Android 6.0+
            Dexter.withContext(activity)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {

                            try {
                                mCityListObservable = getDataFromJson("city_list.json");

                                Disposable disposable =
                                        mCityListObservable
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeWith(getCitiesObserver());

                                compositeDisposable.add(disposable);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Timber.e("Permission denied");
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(
                                PermissionRequest permission,
                                PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .check();
        } else {
            Timber.e("device's sdk version is : %s", Build.VERSION.SDK_INT);
        }
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

            InputStream is = mAssetManager.open(fileName);

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

        Date date = new Date(millis); DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
}
