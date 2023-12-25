package com.riders.thelab.core.ui.compose.base

import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var version: String by mutableStateOf("")
        private set
    var isDarkMode: Boolean by mutableStateOf(true)
        private set
    var isVibration: Boolean by mutableStateOf(true)
        private set
    var viewPagerDotExpanded: Boolean by mutableStateOf(true)
        private set
    var viewPagerDotVisibility: Boolean by mutableStateOf(true)
        private set
    var viewPagerCurrentIndex: Int by mutableStateOf(0)
        private set

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }

    fun updateDarkMode(darkMode: Boolean) {
        this.isDarkMode = darkMode
    }

    fun updateVibration(isVibration: Boolean) {
        this.isVibration = isVibration
    }

    private fun updateVersion(appVersion: String) {
        this.version = appVersion
    }

    fun updateViewPagerExpanded(expanded: Boolean) {
        this.viewPagerDotExpanded = expanded
    }
    fun updateViewPagerDotVisibility(visible: Boolean) {
        this.viewPagerDotVisibility = visible
    }
    fun onCurrentPageChanged(pageChangedIndex: Int) {
        this.viewPagerCurrentIndex = pageChangedIndex
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