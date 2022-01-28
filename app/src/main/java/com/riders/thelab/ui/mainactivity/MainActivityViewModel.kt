package com.riders.thelab.ui.mainactivity

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.utils.LabAddressesUtils
import com.riders.thelab.core.utils.LabLocationUtils
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.data.local.model.app.LocalApp
import com.riders.thelab.data.local.model.app.PackageApp
import com.riders.thelab.data.local.model.weather.ProcessedWeather
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()

    private val progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val imagesFetchedDone: MutableLiveData<Boolean> = MutableLiveData()
    private val imagesFetchedFailed: MutableLiveData<Boolean> = MutableLiveData()
    private val imageUrl: MutableLiveData<String> = MutableLiveData()

    private val weather: MutableLiveData<ProcessedWeather> = MutableLiveData()

    private val applications: MutableLiveData<List<App>> = MutableLiveData()
    private val whatsNewApps: MutableLiveData<List<App>> = MutableLiveData()


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

    fun getProgressVisibility(): LiveData<Boolean> = progressVisibility
    fun getImagesFetchedDone(): LiveData<Boolean> = imagesFetchedDone
    fun getImagesFetchedFailed(): LiveData<Boolean> = imagesFetchedFailed
    fun getImageUrl(): LiveData<String> = imageUrl

    fun getWeather(): LiveData<ProcessedWeather> = weather

    fun getApplications(): LiveData<List<App>> = applications
    fun getWhatsNewApp(): LiveData<List<App>> = whatsNewApps

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
    fun checkConnection(context: Context) {
        connectionStatus.value = LabNetworkManagerNewAPI.getInstance(context).isOnline()
    }

    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    fun getWallpaperImages(context: FragmentActivity) {
        Timber.d("getFirebaseFiles()")
        progressVisibility.value = true

        viewModelScope.launch(ioContext) {
            try {
                val storageReference: StorageReference? = repository.getStorageReference(context)

                storageReference?.let {
                    withContext(mainContext) {
                        // Create a child reference
                        // imagesRef now points to "images"
                        val imagesRef: StorageReference = it.child("images/dark_theme")
                        imagesRef.list(5)
                            .addOnSuccessListener { listResult: ListResult ->
                                Timber.d("onSuccess()")
                                val max = listResult.items.size

                                // Get random int
                                val iRandom = Random().nextInt(max)

                                // Get item url using random int
                                val item = listResult.items[iRandom]

                                // Make rest call
                                item
                                    .downloadUrl
                                    .addOnSuccessListener { uri: Uri ->
                                        imageUrl.value = uri.toString()
                                    }
                            }
                            .addOnFailureListener { t: Exception? ->
                                Timber.e(t)
                                imagesFetchedFailed.value = true
                                progressVisibility.value = false
                            }
                            .addOnCompleteListener { task1: Task<ListResult> ->
                                Timber.d("onComplete() - ${task1.result.items.size}")
                                imagesFetchedDone.value = true
                                progressVisibility.value = false
                            }
                    }
                }
            } catch (throwable: Exception) {
                Timber.e(throwable)
            }
        }
    }


    // Suspend functions are only allowed to be called from a coroutine or another suspend function.
    // You can see that the async function which includes the keyword suspend.
    // So, in order to use that, we need to make our function suspend too.
    fun fetchWeather(context: Context, latitude: Double, longitude: Double) {
        Timber.d("suspend fetchWeather()")
        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {

                    val fetchWeather =
                        repository.getWeatherOneCallAPI(Location("").apply {
                            this.latitude = latitude
                            this.longitude = longitude
                        }) // fetch on IO thread

                    if (null == fetchWeather) {
                        Timber.e("Fetch weather error")
                    } else {
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

                        val mProcessedWeather =
                            ProcessedWeather(cityName, cityCountry, fetchWeather)

                        // back on UI thread
                        withContext(mainContext) {
                            weather.value = mProcessedWeather
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

    fun retrieveApplications(context: Context) {
        Timber.d("retrieveApplications()")
        val constants = Constants(context)
        val appList: MutableList<App> = ArrayList()

        // Get constants activities
        appList.addAll(constants.getActivityList())
        appList.addAll(repository.getPackageList())

        //  val tempList = repository.getAppListFromAssets()

        if (appList.isEmpty()) {
            Timber.e("app list is empty")
        } else {
            applications.value = appList
        }
    }

    fun retrieveRecentApps(context: Context) {
        Timber.d("fetchRecentApps()")

        val constants = Constants(context)

        // Setup last 3 features added
        val mWhatsNewApps = constants
            .getActivityList()
            .take(3)
            .sortedByDescending { (it as LocalApp).date }

        this.whatsNewApps.value = mWhatsNewApps
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
                if (null != item.localActivity) {
                    Timber.d("launchActivity(%s)", item.localActivity.simpleName)
                    launchActivity(navigator, item.localActivity)
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

    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}