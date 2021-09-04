package com.riders.thelab.ui.mainactivity

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()

    private val applications: MutableLiveData<List<App>> = MutableLiveData()


    fun getConnectionStatus(): LiveData<Boolean> {
        return connectionStatus
    }

    fun getLocationData(): LiveData<Boolean> {
        Timber.d("getLocationData()")

        // for simplicity return data directly to view
        return repository.getLocationStatusData()
    }

    fun getApplications(): LiveData<List<App>> {
        return applications
    }


    fun checkConnection() {
        connectionStatus.value = LabNetworkManagerNewAPI.isConnected
    }


    fun addDataSource(locationStatus: LiveData<Boolean>) {
        Timber.d("addLocationStatusDataSource()")
        repository.addLocationStatusDataSource(locationStatus)
    }

    fun removeDataSource(locationStatus: LiveData<Boolean>) {
        Timber.e("removeLocationStatusDataSource()")
        repository.removeLocationStatusDataSource(locationStatus)
    }

    fun retrieveApplications(context: Context) {
        val appList: MutableList<App> = ArrayList()

        // Get constants activities
        appList.addAll(Constants(context).getActivityList())
        appList.addAll(repository.getPackageList())

        if (appList.isEmpty()) {
            Timber.e("app list is empty")
        } else {
            applications.value = appList
        }
    }

    fun launchActivityOrPackage(navigator: Navigator, item: App) {
        if (item.appPackageName.isNotBlank()) {
            Timber.d("launchIntentForPackage(%s)", item.appPackageName)

            // Just use these following two lines,
            // so you can launch any installed application whose package name is known:
            launchIntentForPackage(navigator, item.appPackageName)
        } else {

            // Prevent app from crashing if you click on WIP item
            if (null != item.appActivity) {
                Timber.d("launchActivity(%s)", item.appActivity!!.simpleName)
                launchActivity(navigator, item.appActivity!!)
            } else {
                // Just Log wip item
                Timber.e("Cannot launch this activity : %s", item.toString())
            }
        }
    }


    private fun launchIntentForPackage(navigator: Navigator, packageName: String) {
        navigator.callIntentForPackageActivity(packageName)
    }

    private fun launchActivity(navigator: Navigator, activity: Class<out Activity>) {
        navigator.callIntentActivity(activity)
    }
}