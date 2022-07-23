package com.riders.thelab.ui.splashscreen

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class SplashScreenViewModel : ViewModel() {

    private val appVersion: MutableLiveData<String> = MutableLiveData()
    private val onVideoEnd: MutableLiveData<Boolean> = MutableLiveData()


    fun getAppVersion(): LiveData<String> {
        return appVersion
    }

    fun getOnVideoEnd(): LiveData<Boolean> {
        return onVideoEnd
    }

    fun retrieveAppVersion(activity: SplashScreenActivity) {
        try {
            val pInfo: PackageInfo =
                activity
                    .packageManager
                    .getPackageInfo(activity.packageName, 0)
            val version = pInfo.versionName

            appVersion.value = version

        } catch (error: PackageManager.NameNotFoundException) {
            Timber.e(error)
        }
    }

    fun onVideoEnd() {
        onVideoEnd.value = true
    }
}