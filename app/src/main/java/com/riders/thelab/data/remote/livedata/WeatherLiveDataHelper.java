package com.riders.thelab.data.remote.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class WeatherLiveDataHelper {
    
    private WeatherLiveDataHelper() {
    }

    private static WeatherLiveDataHelper liveDataHelper;
    private MediatorLiveData<Integer> downloadPercent =
            new MediatorLiveData<>();

    synchronized public static WeatherLiveDataHelper getInstance() {
        if (liveDataHelper == null)
            liveDataHelper = new WeatherLiveDataHelper();
        return liveDataHelper;
    }

    public void updateDownloadPer(int percentage) {
        downloadPercent.postValue(percentage);
    }

    public LiveData<Integer> observePercentage() {
        return downloadPercent;
    }
}
