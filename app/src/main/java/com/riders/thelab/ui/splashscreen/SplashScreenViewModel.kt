package com.riders.thelab.ui.splashscreen

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(activity: SplashScreenActivity) : ViewModel() {

    private var mContext: SplashScreenActivity = activity

    private val appVersion: MutableLiveData<String> = MutableLiveData()
    private val onVideoEnd: MutableLiveData<Boolean> = MutableLiveData()


    fun getAppVersion(): LiveData<String> {
        return appVersion
    }

    fun getOnVideoEnd(): LiveData<Boolean> {
        return onVideoEnd;
    }


    fun retrieveAppVersion() {

        try {
            val pInfo: PackageInfo =
                    mContext
                            .packageManager
                            .getPackageInfo(mContext.packageName, 0)
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