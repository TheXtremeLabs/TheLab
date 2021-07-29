package com.riders.thelab.data.remote.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class WeatherLiveDataHelper {

    companion object {
        private var liveDataHelper: WeatherLiveDataHelper? = null

        @Synchronized
        fun getInstance(): WeatherLiveDataHelper? {
            if (liveDataHelper == null) liveDataHelper = WeatherLiveDataHelper()
            return liveDataHelper
        }
    }

    private val downloadPercent = MediatorLiveData<Int>()

    fun updateDownloadPer(percentage: Int) {
        downloadPercent.postValue(percentage)
    }

    fun observePercentage(): LiveData<Int> {
        return downloadPercent
    }
}