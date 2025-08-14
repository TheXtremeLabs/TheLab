package com.riders.thelab.ui.mainactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.location.Address
import android.location.Geocoder
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.common.network.NetworkState
import com.riders.thelab.core.common.utils.LabAddressesUtils
import com.riders.thelab.core.common.utils.LabAppManager
import com.riders.thelab.core.common.utils.LabAppManager.getPackageList
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.data.local.model.weather.ProcessedWeather
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.core.speechtotext.SpeechToTextRepository
import com.riders.thelab.core.speechtotext.VoiceManagedViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.core.ui.utils.toBitmap
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.utils.AppBuilderUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.UnknownHostException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    labNetworkManager: LabNetworkManager,
    val uiRepository: IUiRepository,
    val speechToTextRepository: SpeechToTextRepository
) : VoiceManagedViewModel(speechToTextRepository), DefaultLifecycleObserver {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var mNavigator: Navigator? = null

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    // App Lists
    var whatsNewAppList = mutableStateListOf<LocalApp>()
    var appList = mutableStateListOf<App>()

    val filteredList: List<App> by derivedStateOf {
        if (searchedAppRequest.trim().isBlank() || searchedAppRequest.trim() == "") {
            appList.toList()
        } else {
            appList.toList().filter {
                (it.appName != null && it.appName?.contains(
                    searchedAppRequest, ignoreCase = true
                )!!) || (it.appTitle != null && it.appTitle?.contains(
                    searchedAppRequest, ignoreCase = true
                )!!)
            }
        }
    }

    // Dynamic Island
    private val _dynamicIslandState: MutableStateFlow<IslandState> =
        MutableStateFlow(IslandState.DefaultState)
    val dynamicIslandState: StateFlow<IslandState> = _dynamicIslandState

    val isDynamicIslandVisible: Boolean by derivedStateOf { _dynamicIslandState.value !is IslandState.DefaultState }

    // Network
    private lateinit var networkState: StateFlow<NetworkState>

    // Network State
    var hasInternetConnection: StateFlow<Boolean> = labNetworkManager.isConnectedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    // ViewPager Scroll
    var isPagerAutoScroll: Boolean by mutableStateOf(false)
        private set

    // Search
    var searchedAppRequest by mutableStateOf("")
        private set
    var keyboardVisible by mutableStateOf(false)
        private set
    var isMicrophoneEnabled by mutableStateOf(false)
        private set


    // App List
    private fun updateWhatsNewList(whatsNewList: List<LocalApp>) {
        this.whatsNewAppList.clear()
        this.whatsNewAppList.addAll(whatsNewList)
    }

    private fun updateAppList(appList: List<App>) {
        this.appList.clear()
        this.appList.addAll(appList)
    }

    // Dynamic Island
    fun updateDynamicIslandState(newIslandState: IslandState) {
        viewModelScope.launch {
            _dynamicIslandState.value = newIslandState

            if (newIslandState is IslandState.NetworkState.Available ||
                newIslandState is IslandState.NetworkState.Lost ||
                newIslandState is IslandState.NetworkState.Unavailable
            ) {
                delay(5_000L)
                _dynamicIslandState.value = IslandState.DefaultState
                updateKeyboardVisible(false)
            }
        }
    }

    // Search
    fun updateSearchAppRequest(inputApp: String) {
        this.searchedAppRequest = inputApp
    }

    fun updateKeyboardVisible(isVisible: Boolean) {
        keyboardVisible = isVisible
    }

    fun updateMicrophoneEnabled(isMicEnabled: Boolean) {
        this.isMicrophoneEnabled = isMicEnabled
    }

    /*fun updateDynamicIslandVisible(visible: Boolean) {
        this.isDynamicIslandVisible = visible
    }*/

    fun updatePagerAutoScroll(autoScroll: Boolean) {
        this.isPagerAutoScroll = autoScroll
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("Coroutine Exception caught with message: ${throwable.message} (${throwable.javaClass})")

        }


    //////////////////////////////////
    //
    // OVERRIDE
    //
    //////////////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
        speechToTextRepository.release()
    }


    //////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////
    fun initNavigator(activity: Activity) {
        Timber.d("initNavigator()")
        mNavigator = Navigator(activity)
    }


    fun onEvent(event: UiEvent) {
        Timber.d("onEvent() | event : $event")

        when (event) {
            is UiEvent.OnUpdateDynamicIslandState -> updateDynamicIslandState(event.newState)
            is UiEvent.OnUpdateDynamicIslandVisible -> {
                if (!event.isVisible) {
                    updateDynamicIslandState(IslandState.DefaultState)
                    return
                }
            }

            is UiEvent.OnSearchClicked -> updateDynamicIslandState(IslandState.SearchState())
            is UiEvent.OnUpdateSearchQuery -> updateSearchAppRequest(event.newQuery)

            is UiEvent.OnMicrophoneClicked -> {}
            is UiEvent.OnUpdateMicrophoneEnabled -> updateMicrophoneEnabled(event.enabled)

            is UiEvent.OnSettingsClicked -> {
                mNavigator?.callSettingsActivity()
                    ?: run { Timber.e("onEvent() | navigator object is null") }
            }

            else -> {
                Timber.e("Unknown event for $event")
            }
        }
    }

    // Suspend functions are only allowed to be called from a coroutine or another suspend function.
    // You can see that the async function which includes the keyword suspend.
    // So, in order to use that, we need to make our function suspend too.
    @SuppressLint("NewApi")
    fun fetchWeather(context: Context, latitude: Double, longitude: Double) {
        Timber.d("suspend fetchWeather()")
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            try {
                supervisorScope {

                    val fetchWeather: OneCallWeatherResponse? = null
                    /*repository.getWeatherOneCallAPI(Location("").apply {
                        this.latitude = latitude
                        this.longitude = longitude
                    }) */
                    // fetch on IO thread

                    if (null == fetchWeather) {
                        Timber.e("Fetch weather error")
                    } else {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val weatherLocation =
                            (fetchWeather.latitude to fetchWeather.longitude).toLocation()

                        if (!LabCompatibilityManager.isTiramisu()) {
                            LabAddressesUtils.getDeviceAddressLegacy(geocoder, weatherLocation)
                                ?.let { address ->
                                    buildProcessWeather(fetchWeather, address)
                                } ?: run { Timber.e("address object is null") }
                        } else {
                            // back on UI thread
                            withContext(Dispatchers.Main) {
                                LabAddressesUtils.getDeviceAddressAndroid13(
                                    geocoder,
                                    weatherLocation
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

    private fun buildProcessWeather(fetchWeather: OneCallWeatherResponse, address: Address) {
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
                ProcessedWeather(
                    cityName,
                    cityCountry,
                    temp,
                    icon
                )
            } ?: run { Timber.e("Weather icon object is null") }
        } ?: run { Timber.e("Temperature object is null") }
    }


    fun retrieveApplications(context: Context) {
        Timber.d("retrieveApplications()")
        val appList: MutableList<App> = ArrayList()

        // Get constants activities
        appList.addAll(AppBuilderUtils.buildActivities(context))
        appList.addAll(context.getPackageList { appInfo ->

            val icon: Drawable =
                context.packageManager.getApplicationIcon(appInfo.packageName)
            val pInfo: PackageInfo =
                context.packageManager.getPackageInfo(appInfo.packageName, 0)
            val version: String? = pInfo.versionName
            val packageName: String? = appInfo.packageName

            version?.let { appVersion ->
                packageName?.let { appPackageName ->
                    appList.add(
                        PackageApp(
                            context.packageManager.getApplicationLabel(appInfo).toString(),
                            icon,
                            appVersion,
                            appPackageName
                        )
                    )
                } ?: run {
                    Timber.e("Unable to get app package name")
                }
            } ?: run {
                Timber.e("Unable to get app version")
            }
        })

        if (appList.isEmpty()) {
            Timber.e("app list is empty")
        } else {
            updateAppList(appList)
        }
    }

    fun retrieveRecentApps(context: Context) {
        // Setup last 3 features added
        val mWhatsNewApps: List<LocalApp> = AppBuilderUtils
            .buildActivities(context)
            .sortedByDescending { (it as LocalApp).appDate }
            .take(3)
            .map {
                val bitmap: Bitmap? = when (it.appDrawableIcon) {
                    is BitmapDrawable -> {
                        (it.appDrawableIcon as BitmapDrawable).bitmap as Bitmap
                    }

                    is VectorDrawable -> {
                       (it.appDrawableIcon as VectorDrawable).toBitmap()
                    }

                    else -> {
                        null
                    }
                }

                LocalApp(
                    it.id,
                    it.appTitle!!,
                    it.appDescription!!,
                    null,
                    it.appActivity,
                    it.appDate!!
                ).apply {
                    this.bitmap = bitmap
                }
            }

        updateWhatsNewList(mWhatsNewApps)
    }

    fun launchActivityOrPackage(item: App) {
        Timber.d("launchActivityOrPackage()")

        when (item) {
            is PackageApp -> {
                if (!item.appPackageName.isNullOrBlank()) {
                    Timber.d("launchIntentForPackage(%s)", item.appPackageName)

                    // Just use these following two lines,
                    // so you can launch any installed application whose package name is known:
                    launchIntentForPackage(item.appPackageName!!)
                } else {
                    Timber.e("Cannot launch activity with this package name : ${item.appPackageName}")
                }
            }

            is LocalApp -> {
                if (null != item.appActivity) {
                    Timber.d("launchActivity(%s)", item.appActivity!!.simpleName)
                    launchActivity(item.appActivity!!)
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

    private fun launchIntentForPackage(packageName: String) {
        mNavigator?.callIntentForPackageActivity(packageName)
    }

    private fun launchActivity(activity: Class<out Activity>) {
        mNavigator?.callIntentActivity(activity)
    }


    //////////////////////////////////
    //
    // IMPLEMENTS METHODS
    //
    //////////////////////////////////
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")
        // TODO : Uncomment
        // speechToTextRepository.startRecognition()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Timber.e("onPause()")
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Timber.d("onResume()")

        viewModelScope.launch {
            // Retrieve applications
            retrieveApplications(context = context)

            delay(300L)
            retrieveRecentApps(context = context)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onStop()")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onDestroy()")
    }
}