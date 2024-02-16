package com.riders.thelab.core.ui.compose.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import timber.log.Timber

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
/*
 * To do that we will write an extension Composable function for ViewModel which
 * will receive Composable lifecycle Owner LocalLifecycleOwner.current.lifecycle
 * and will add observer and remove observer on onDispose block.
 *
 * The ViewModel will implement DefaultLifecycleObserver and will start receiving lifecycle events.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <viewModel : LifecycleObserver> viewModel.observeLifecycleEvents(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@observeLifecycleEvents)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycleEvents)
        }
    }
}

@Suppress("EmptyMethod")
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