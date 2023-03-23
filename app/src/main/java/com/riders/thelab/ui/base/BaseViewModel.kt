package com.riders.thelab.ui.base

import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import timber.log.Timber

open class BaseViewModel : ViewModel() {
    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var version by mutableStateOf("")
        private set

    fun updateVersion(appVersion: String) {
        version = appVersion
    }


    //////////////////////////////////////////
    // Class Methods
    //////////////////////////////////////////
    fun retrieveAppVersion(activity: Activity) {
        try {
            val pInfo: PackageInfo =
                activity
                    .packageManager
                    .getPackageInfo(activity.packageName, 0)
            val version = pInfo.versionName

            updateVersion(version)

        } catch (error: PackageManager.NameNotFoundException) {
            Timber.e(error)
        }
    }
}