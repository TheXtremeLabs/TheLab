package com.riders.thelab.ui.mainactivity

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.utils.LabAddressesUtils
import com.riders.thelab.core.utils.LabLocationUtils
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.data.local.model.weather.ProcessedWeather
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    private val weather: MutableLiveData<ProcessedWeather> = MutableLiveData()
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

    fun getWeather(): LiveData<ProcessedWeather> {
        return weather
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

    // Suspend functions are only allowed to be called from a coroutine or another suspend function.
    // You can see that the async function which includes the keyword suspend.
    // So, in order to use that, we need to make our function suspend too.
    fun fetchWeather(context: Context, latitude: Double, longitude: Double) {
        Timber.d("suspend fetchWeather()")
        viewModelScope.launch {
            try {
                supervisorScope {

                    val fetchWeather =
                        repository.getWeatherOneCallAPI(Location("").apply {
                            this.latitude = latitude
                            this.longitude = longitude
                        }) // fetch on IO thread

                    val address: Address? =
                        LabAddressesUtils.getDeviceAddress(
                            Geocoder(context, Locale.getDefault()),
                            LabLocationUtils.buildTargetLocationObject(
                                fetchWeather.latitude,
                                fetchWeather.longitude
                            )
                        )
                    val cityName: String =
                        address?.locality.orEmpty()
                    val cityCountry: String = address?.countryName.orEmpty()

                    val mProcessedWeather = ProcessedWeather(cityName, cityCountry, fetchWeather)

                    // back on UI thread
                    weather.value = mProcessedWeather
                }
            } catch (exception: Exception) {
                if (exception is UnknownHostException) {
                    Timber.e("Check your connection cannot reach host")
                }

                Timber.e(exception.message)
            }
        }

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


    fun fetchRecentApps(context: Context, recentAppsNames: Array<String>): List<App> {
        val recentAppList: MutableList<App> = ArrayList()

        val constants = Constants(context)

        // Setup last 3 features added
        for (element in constants.getActivityList()) {
            for (item in recentAppsNames) {
                if (element.appTitle.contains(item))
                    (recentAppList as ArrayList<App>).add(element)
            }
        }

        return recentAppList
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