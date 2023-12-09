package com.riders.thelab.ui.mainactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.network.LabNetworkManagerNewAPI
import com.riders.thelab.core.common.utils.LabAddressesUtils
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabLocationUtils
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.data.local.model.weather.ProcessedWeather
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.UnknownHostException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()

    private val weather: MutableLiveData<ProcessedWeather> = MutableLiveData()

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }


    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var searchedAppRequest = mutableStateOf("")
    var keyboardVisible = mutableStateOf(false)

    // Backing property to avoid state updates from other classes
    private val _whatsNewAppList: MutableStateFlow<List<App>> =
        MutableStateFlow(emptyList())

    // The UI collects from this StateFlow to get its state updates
    val whatsNewAppList: StateFlow<List<App>> = _whatsNewAppList

    // Backing property to avoid state updates from other classes
    private val _appList: MutableStateFlow<List<App>> =
        MutableStateFlow(emptyList())

    // The UI collects from this StateFlow to get its state updates
    val appList: StateFlow<List<App>> = _appList

    val dynamicIslandState = mutableStateOf<IslandState>(IslandState.DefaultState())
    fun displayDynamicIsland(isDisplayed: Boolean) {
        dynamicIslandState.value = IslandState.SearchState()
    }

    fun updateKeyboardVisible(isVisible: Boolean) {
        keyboardVisible.value = isVisible
    }

    //////////////////////////////////
    //
    // OBSERVERS
    //
    //////////////////////////////////
    fun getConnectionStatus(): LiveData<Boolean> = connectionStatus
    fun getLocationData(): LiveData<Boolean> {
        Timber.d("getLocationData()")

        // for simplicity return data directly to view
        return repository.getLocationStatusData()
    }

    fun getWeather(): LiveData<ProcessedWeather> = weather


    fun addDataSource(locationStatus: LiveData<Boolean>) {
        Timber.d("addLocationStatusDataSource()")
        repository.addLocationStatusDataSource(locationStatus)
    }

    fun removeDataSource(locationStatus: LiveData<Boolean>) {
        Timber.e("removeLocationStatusDataSource()")
        repository.removeLocationStatusDataSource(locationStatus)
    }


    //////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////
    fun searchApp(requestedAppName: String) {
        searchedAppRequest.value = requestedAppName
    }

    fun checkConnection(context: Context) {
        connectionStatus.value = LabNetworkManagerNewAPI.getInstance(context).isOnline()
    }


    // Suspend functions are only allowed to be called from a coroutine or another suspend function.
    // You can see that the async function which includes the keyword suspend.
    // So, in order to use that, we need to make our function suspend too.
    @SuppressLint("NewApi")
    fun fetchWeather(context: Context, latitude: Double, longitude: Double) {
        Timber.d("suspend fetchWeather()")
        viewModelScope.launch(IO + SupervisorJob() + coroutineExceptionHandler) {
            try {
                supervisorScope {

                    val fetchWeather: OneCallWeatherResponse? =
                        repository.getWeatherOneCallAPI(Location("").apply {
                            this.latitude = latitude
                            this.longitude = longitude
                        }) // fetch on IO thread

                    if (null == fetchWeather) {
                        Timber.e("Fetch weather error")
                    } else {

                        if (!LabCompatibilityManager.isTiramisu()) {
                            LabAddressesUtils.getDeviceAddressLegacy(
                                Geocoder(context, Locale.getDefault()),
                                LabLocationUtils.buildTargetLocationObject(
                                    fetchWeather.latitude,
                                    fetchWeather.longitude
                                )
                            )?.let { address ->

                                buildProcessWeather(fetchWeather, address)

                            } ?: run {
                                Timber.e("address object is null")
                            }
                        } else {
                            // back on UI thread
                            withContext(Main) {
                                LabAddressesUtils.getDeviceAddressAndroid13(
                                    Geocoder(context, Locale.getDefault()),
                                    LabLocationUtils.buildTargetLocationObject(
                                        fetchWeather.latitude,
                                        fetchWeather.longitude
                                    )
                                ) { address ->
                                    address?.let {
                                        buildProcessWeather(fetchWeather, it)
                                    } ?: run { Timber.e("address object is null") }
                                }
                            }
                        }
                    }
                }
            } catch (exception: Exception) {
                if (exception is UnknownHostException) {
                    Timber.e("Check your connection cannot reach host")
                }

                Timber.e(exception.message)
            }
        }
    }

    fun buildProcessWeather(fetchWeather: OneCallWeatherResponse, address: Address) {
        Timber.d("buildProcessWeather()")

        val cityName: String =
            address.locality.orEmpty()
        val cityCountry: String = address.countryName.orEmpty()
        val temperature: Int? =
            fetchWeather.currentWeather?.temperature?.toInt()
        val weatherIconUrl: String? =
            fetchWeather.currentWeather?.weather?.get(0)?.icon

        temperature?.let { temp ->
            weatherIconUrl?.let { icon ->

                val mProcessedWeather =
                    ProcessedWeather(
                        cityName,
                        cityCountry,
                        temp,
                        icon
                    )

                setProcessedWeather(mProcessedWeather)
            } ?: run { Timber.e("Weather icon object is null") }
        } ?: run { Timber.e("Temperature object is null") }
    }

    fun setProcessedWeather(processedWeather: ProcessedWeather) {
        Timber.d("setProcessedWeather()")
        weather.value = processedWeather
    }

    fun retrieveApplications(context: Context) {
        Timber.d("retrieveApplications()")
        //val constants = Constants(context)
        val appList: MutableList<App> = ArrayList()

        // Get constants activities
        appList.addAll(Constants.getActivityList(context))
        appList.addAll(repository.getPackageList(context))

        //  val tempList = repository.getAppListFromAssets()

        if (appList.isEmpty()) {
            Timber.e("app list is empty")
        } else {
            _appList.value = appList
        }
    }

    fun retrieveRecentApps(context: Context) {
        Timber.d("fetchRecentApps()")

        // Setup last 3 features added
        val mWhatsNewApps = Constants
            .getActivityList(context)
            .sortedByDescending { (it as LocalApp).appDate }
            .take(3)

        _whatsNewAppList.value = mWhatsNewApps
    }

    fun launchActivityOrPackage(navigator: Navigator, item: App) {
        Timber.d("launchActivityOrPackage()")

        when (item) {
            is PackageApp -> {
                if (!item.appPackageName.isNullOrBlank()) {
                    Timber.d("launchIntentForPackage(%s)", item.appPackageName)

                    // Just use these following two lines,
                    // so you can launch any installed application whose package name is known:
                    launchIntentForPackage(navigator, item.appPackageName!!)
                } else {
                    Timber.e("Cannot launch activity with this package name : ${item.appPackageName}")
                }
            }

            is LocalApp -> {
                if (null != item.appActivity) {
                    Timber.d("launchActivity(%s)", item.appActivity!!.simpleName)
                    launchActivity(navigator, item.appActivity!!)
                } else {
                    // Just Log wip item
                    Timber.e("Cannot launch this activity : %s", item.toString())
                }
            }

            else -> {
                Timber.e("else branch")
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