package com.riders.thelab.core.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.ui.weather.WeatherDownloadWorker;

import javax.inject.Inject;
import javax.inject.Singleton;

//@Singleton
public class LabWorkerFactory extends WorkerFactory {

    LabService service;

//    @Inject
    public LabWorkerFactory(LabService service) {
        this.service = service;
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(@NonNull Context appContext,
                                         @NonNull String workerClassName,
                                         @NonNull WorkerParameters workerParameters) {

//        if (workerClassName.equals(WeatherDownloadWorker.class.getSimpleName()))
//            return new WeatherDownloadWorker(appContext, workerParameters, workerClassName, service);

//        else
            // Return null, so that the base class can delegate to the default WorkerFactory.
            return null;
    }
}
