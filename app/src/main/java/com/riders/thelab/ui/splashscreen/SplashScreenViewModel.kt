package com.riders.thelab.ui.splashscreen

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import timber.log.Timber

class SplashScreenViewModel : ViewModel() {

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var version = mutableStateOf("")
    val videoViewVisibility = mutableStateOf(true)
    val splashLoadingContentVisibility = mutableStateOf(false)
    val startCountDown = mutableStateOf(false)
    fun updateVideoViewVisibility(isVisible: Boolean) {
        videoViewVisibility.value = isVisible
    }

    fun updateSplashLoadingContentVisibility(isVisible: Boolean) {
        splashLoadingContentVisibility.value = isVisible
    }

    fun updateStartCountDown(started: Boolean) {
        startCountDown.value = started
    }

    //////////////////////////////////////////
    // Class Methods
    //////////////////////////////////////////
    fun retrieveAppVersion(activity: SplashScreenActivity) {
        try {
            val pInfo: PackageInfo =
                activity
                    .packageManager
                    .getPackageInfo(activity.packageName, 0)
            val version = pInfo.versionName

            this.version.value = version

        } catch (error: PackageManager.NameNotFoundException) {
            Timber.e(error)
        }
    }
}