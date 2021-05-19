package com.riders.thelab.ui.mainactivity

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.utils.LabNetworkManager
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.model.App
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
        private val  navigator: Navigator
) : ViewModel() {

    private val applications: MutableLiveData<List<App>> = MutableLiveData()

    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()
/*
    @Inject
    lateinit var navigator: Navigator*/

    @Inject
    private lateinit var repository: RepositoryImpl

    fun getApplications(): LiveData<List<App>> {
        return applications
    }

    fun getConnectionStatus(): LiveData<Boolean> {
        return connectionStatus
    }

    fun checkConnection(context: MainActivity) {
        connectionStatus.value = LabNetworkManager.isConnected(context)
    }

    fun retrieveApplications() {
        val appList: MutableList<App> = ArrayList()

        // Get constants activities
        appList.addAll(Constants.getInstance().getActivityList())
        appList.addAll(repository.getPackageList())

        if (appList.isEmpty()) {
            Timber.e("app list is empty")
        } else {
            applications.value = appList
        }
    }

    fun launchActivityOrPackage(item: App) {

        if (null != item.appPackageName) {
            Timber.d("launchIntentForPackage(%s)", item.appPackageName)

            // Just use these following two lines,
            // so you can launch any installed application whose package name is known:
            launchIntentForPackage(item.appPackageName!!)
        } else {

            // Prevent app from crashing if you click on WIP item
            if (null != item.appActivity) {
                Timber.d("launchActivity(%s)", item.appActivity!!.simpleName)
                launchActivity(item.appActivity!!)
            } else {
                // Just Log wip item
                Timber.e("Cannot launch this activity : %s", item.toString())
            }
        }
    }


    private fun launchIntentForPackage(packageName: String) {
        navigator.callIntentForPackageActivity(packageName)
    }

    private fun launchActivity(activity: Class<out Activity>) {
        navigator.callIntentActivity(activity)
    }
}