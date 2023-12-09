package com.riders.thelab.ui.splashscreen

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.riders.thelab.R
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.utils.Constants
import timber.log.Timber

class SplashScreenViewModel : ViewModel() {

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var version = mutableStateOf("")
    val startCountDown = mutableStateOf(false)

    var videoPath: String? by mutableStateOf(null)
        private set
    var switchContent: Boolean by mutableStateOf(false)
        private set


    fun updateStartCountDown(started: Boolean) {
        startCountDown.value = started
    }

    fun updateVideoPath(path: String) {
        this.videoPath = path
    }

    fun updateSwitchContent(switch: Boolean) {
        this.switchContent = switch
    }

    //////////////////////////////////////////
    // Class Methods
    //////////////////////////////////////////
    fun getVideoPath(activity: SplashScreenActivity): String? = try {
        val videoPath =
            Constants.ANDROID_RES_PATH +
                    activity.packageName.toString() +
                    Constants.SEPARATOR +
                    //Smartphone portrait video or Tablet landscape video
                    if (!LabCompatibilityManager.isTablet(activity)) R.raw.splash_intro_testing_sound_2 else R.raw.splash_intro_testing_no_sound_tablet
        updateVideoPath(videoPath)
        videoPath
    } catch (exception: Exception) {
        exception.printStackTrace()
        Timber.e(exception.message)
        null
    }

    fun retrieveAppVersion(activity: SplashScreenActivity) = try {
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