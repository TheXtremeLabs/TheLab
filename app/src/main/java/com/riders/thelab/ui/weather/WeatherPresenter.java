package com.riders.thelab.ui.weather;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.content.ContextCompat;
import androidx.room.EmptyResultSetException;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabAddressesUtils;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.LabLocationManager;
import com.riders.thelab.core.utils.LabNetworkManager;
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.local.LabRepository;
import com.riders.thelab.data.local.model.weather.WeatherData;
import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.ui.base.BasePresenterImpl;
import com.riders.thelab.utils.Constants;

import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class WeatherPresenter extends BasePresenterImpl<WeatherView>
        implements WeatherContract.Presenter {

    public static final String MESSAGE_STATUS = "message_status";
    public static final String URL_REQUEST = "url_request";
    private static final String WORK_RESULT = "work_result";
    // CompositeDisposable can maintain list of subscriptions in  pool
    // and can dispose them all at once
    // Disposing them in Destroy one bye one is a tedious task and it can be error
    // prone as you might forgot to dispose.
    // In this case we can use CompositeDisposable.
    private final CompositeDisposable compositeDisposable;

    @Inject
    WeatherActivity activity;
    @Inject
    LabRepository repository;

    /* RxJava / RxAndroid */

    // Disposable is used to dispose the subscription when an Observer
    // no longer wants to listen to Observable
    // To prevent memory leak, for example when a task is done
    // and the activity/fragment is already destroyed,
    // the observer try to update the activity/fragment's UI that has been destroyed
    @Inject
    LabService service;

    private LabLocationManager labLocationManager;


    @Inject
    WeatherPresenter() {
        compositeDisposable = new CompositeDisposable();
    }


    @Override
    public boolean canGetLocation() {
        labLocationManager = new LabLocationManager(activity, activity);
        return labLocationManager.canGetLocation();
    }

    @Override
    public Cursor getCityQuery(String cityQuery) {
        return repository.getCitiesCursor(cityQuery);
    }


    @SuppressLint("NewApi")
    @Override
    public void getCitiesData() {

        if (!LabCompatibilityManager.isLollipop()) {
            if (!LabNetworkManager.isConnected(activity)) {
                Timber.e("No Internet connection");

                getView().onNoConnectionDetected();
                UIManager.showActionInToast(activity, activity.getResources().getString(R.string.network_status_disconnected));
                return;
            }

        } else {
            if (!LabNetworkManagerNewAPI.isConnected(activity)) {
                getView().hideLoader();
                getView().onNoConnectionDetected();
                return;
            }
        }

        getView().showLoader();

        // First step
        // Call repository to check if there is data in database
        Disposable disposable =
                repository.getWeatherData()
                        .subscribe(
                                weatherData -> {

                                    if (!weatherData.isWeatherData()) {

                                        // In this case record's return is null
                                        // then we have to call our Worker to perform
                                        // the web service call to retrieve data from api
                                        Timber.e("List is empty. No Record found in database");

                                        // Only for debug purposes
                                        // Use worker to make long job operation in background
                                        Timber.e("Use worker to make long job operation in background...");
                                        startWork();
                                    } else {
                                        // In this case data already exists in database
                                        // Load data then let the the user perform his request
                                        Timber.d("Record found in database. Continue...");

                                        getView().hideLoader();
                                        getView().onFetchCitySuccessful();
                                    }
                                }, throwable -> {
                                    Timber.e("Error while fetching records in database");

                                    if (throwable instanceof EmptyResultSetException) {
                                        Timber.e(throwable);
                                        Timber.e("weatherData is empty. No Record found in database");

                                        startWork();
                                    } else {
                                        Timber.e(throwable);
                                    }
                                });

        compositeDisposable.add(disposable);
    }

    @Override
    public Address getCityNameWithCoordinates(final double latitude, final double longitude) {
        return LabAddressesUtils
                .getDeviceAddress(
                        new Geocoder(activity, Locale.getDefault()),
                        LabLocationManager.buildTargetLocationObject(latitude, longitude));
    }


    @Override
    public void getCurrentWeather() {
        labLocationManager.getLocation();
    }


    /////////////////////////////////////
    //
    // WORKER
    //
    /////////////////////////////////////

    /**
     * Launch Worker that will manage download and extraction of the cities zip file from bulk openweather server
     */
    private void startWork() {
        Constraints constraints = new Constraints.Builder()
//                .setRequiresBatteryNotLow(true)
//                .setRequiresCharging(false)
//                .setRequiresStorageNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        @SuppressLint("RestrictedApi")
        WorkRequest weatherCitiesWorkRequest =
                new OneTimeWorkRequest.Builder(WeatherDownloadWorker.class)
                        .setConstraints(constraints)
                        .setInputData(
                                new Data.Builder()
                                        .putString(
                                                URL_REQUEST,
                                                Constants.BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD + Constants.WEATHER_BULK_DOWNLOAD_URL)
                                        .build()
                        )
                        .addTag(WeatherDownloadWorker.class.getSimpleName())
                        .build();

        UUID id = weatherCitiesWorkRequest.getId();

        WorkManager
                .getInstance(activity)
                .enqueue(weatherCitiesWorkRequest);

        listenToTheWorker(id);
    }


    private void listenToTheWorker(UUID workerId) {
        Timber.d("listenToTheWorker : %s", workerId);
        WorkManager
                .getInstance(activity)
                .getWorkInfoByIdLiveData(workerId)
                .observe(
                        activity,
                        workInfos -> {

                            switch (workInfos.getState()) {
                                case ENQUEUED:
                                    Timber.d("Worker ENQUEUED");
                                    break;

                                case RUNNING:
                                    Timber.d("Worker RUNNING");

                                    activity.runOnUiThread(() -> getView().updateDownloadStatus("Loading..."));

                                    // Update ui calling this method because
                                    // -> Only the original thread that created a view hierarchy can touch its views.
                                    // Throwing : android.view.ViewRootImpl$CalledFromWrongThreadException

                                    /*ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
                                    scheduledThreadPoolExecutor.schedule(() -> {
                                        getView().updateDownloadStatus("Loading...");
                                        scheduledThreadPoolExecutor.shutdown();
                                    }, 2, TimeUnit.SECONDS);*/
                                    break;

                                case SUCCEEDED:
                                    Disposable disposable =
                                            repository
                                                    .insertWeatherData(new WeatherData(true))
                                                    .subscribe();

                                    compositeDisposable.add(disposable);

                                    getView().hideLoader();
                                    getView().updateDownloadStatus("Loading finished");
                                    getView().onFetchCitySuccessful();
                                    break;

                                case FAILED:
                                    Timber.e("Worker FAILED");
                                    Snackbar
                                            .make(
                                                    activity.findViewById(android.R.id.content),
                                                    "Worker FAILED",
                                                    BaseTransientBottomBar.LENGTH_LONG)
                                            .setTextColor(ContextCompat.getColor(activity, android.R.color.holo_red_light))
                                            .show();
                                    break;

                                case BLOCKED:
                                    Timber.e("Worker BLOCKED");
                                    break;

                                case CANCELLED:
                                    Timber.e("Worker CANCELLED");
                                    break;

                                default:
                                    break;
                            }
                        });
    }


    /////////////////////////////////////
    //
    // RX
    //
    /////////////////////////////////////
    @Override
    public void getWeather(Location location) {
        getView().showLoader();

        Disposable disposable =
                service.getWeatherOneCallAPI(location)
                        .subscribe(
                                weatherResponse -> {
                                    getView().hideLoader();
                                    getView().updateOneCallUI(weatherResponse);
                                },
                                throwable -> {
                                    Timber.e(throwable);
                                    getView().hideLoader();
                                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void clearDisposables() {
        compositeDisposable.clear();
    }

    private void cancelWorker() {
        Timber.e("cancelWorker()");

        Timber.d("Worker is about to be cancelled");
        WorkManager
                .getInstance(activity)
                .cancelAllWork();
    }


    @Override
    public void detachView() {
        super.detachView();

        clearDisposables();
        cancelWorker();
    }
}
