package com.riders.thelab.ui.weather;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.riders.thelab.TheLabApplication;
import com.riders.thelab.core.parser.LabParser;
import com.riders.thelab.core.utils.LabFileManager;
import com.riders.thelab.data.DataModule;
import com.riders.thelab.data.local.model.weather.CityMapper;
import com.riders.thelab.data.remote.dto.weather.City;
import com.riders.thelab.utils.Validator;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@SuppressLint("RestrictedApi")
public class WeatherDownloadWorker extends ListenableWorker {

    public static final String WORK_SUCCESS = "Loading finished";
    public static final String WORK_DOWNLOAD_FAILED = "Error while downloading zip file";
    private static final String WORK_RESULT = "work_result";
    ResolvableFuture<Result> future;
    Data taskData;
    String taskDataString;
    Data outputData;


    public WeatherDownloadWorker(Context context, WorkerParameters params) {
        super(context, params);

        future = ResolvableFuture.create();
    }


    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {

        Timber.d("startWork()");

        taskData = getInputData();
        if (null == taskData) {
            Timber.e("Input Data is null");
        }
        taskDataString = taskData.getString(WeatherPresenter.MESSAGE_STATUS);

        String urlRequest = taskData.getString(WeatherPresenter.URL_REQUEST);
        if (urlRequest == null) {
            future.set(Result.failure());
        }

        TheLabApplication
                .getWeatherRestClient()
                .getBulkApiService()
                .getCitiesGZipFile()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        responseFile -> {
                            Timber.d("observer.onSuccess(responseFile)");
                            try {

                                Timber.d("Unzipped downloaded file...");
                                // Step 1 : Unzip
                                String unzippedGZipResult = LabFileManager.unzipGzip(responseFile);

                                if (Validator.isEmpty(unzippedGZipResult)) {
                                    Timber.e("String unzippedGZipResult is empty");
                                    return;
                                }

                                List<City> dtoCities =
                                        LabParser
                                                .getInstance()
                                                .parseJsonFileListWithMoshi(unzippedGZipResult);

                                if (Validator.isNullOrEmpty(dtoCities)) {
                                    Timber.e("List<City> dtoCities is empty");
                                    return;
                                }


                                Timber.d("Save in database...");
                                // Step 3 save in database
                                saveCities(dtoCities);
                            } catch (Exception e) {
                                Timber.e(e);
                            }

                        },
                        throwable -> {
                            Timber.e(WORK_DOWNLOAD_FAILED);
                            Timber.e(throwable);
                            outputData =
                                    createOutputData(
                                            WORK_RESULT,
                                            WORK_DOWNLOAD_FAILED);
                            future.set(Result.failure(outputData));
                        });
        return future;
    }

    /**
     * Creates ouput data to send back to the activity / presenter which's listening to it
     *
     * @param outputDataKey
     * @param message
     * @return
     */
    private Data createOutputData(String outputDataKey, String message) {
        Timber.d("createOutputData()");
        return new Data.Builder()
                .put(outputDataKey, message)
                .build();
    }


    public void saveCities(List<City> dtoCities) {
        Timber.d("saveCities()");
        DataModule
                .getInstance()
                .getDatabase()
                .getWeatherDao()
                .insertAllRX(CityMapper.getCityList(dtoCities))
                .subscribe(
                        longs -> {
                            outputData = createOutputData(WORK_RESULT, WORK_SUCCESS);
                            future.set(Result.success(outputData));
                        },
                        Timber::e);
    }
}
