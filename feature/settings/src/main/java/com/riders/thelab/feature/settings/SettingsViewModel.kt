package com.riders.thelab.feature.settings

import android.util.DisplayMetrics
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabDeviceManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.DeviceInformation
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@Suppress("EmptyMethod")
@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: IRepository) : BaseViewModel(),
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    val themeOptions: List<String> = listOf("Light", "Dark", "Use System")

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var user: User? by mutableStateOf(null)
        private set

    var deviceInfo: DeviceInformation? by mutableStateOf(null)
        private set
    var showMoreInfoOnDevice: Boolean by mutableStateOf(false)
        private set

    private fun updateUser(user: User) {
        this.user = user
    }

    private fun updateDeviceInfo(deviceInformation: DeviceInformation) {
        this.deviceInfo = deviceInformation
    }

    private fun updateShowMoreInfoOnDevice(showMore: Boolean) {
        this.showMoreInfoOnDevice = showMore
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | ${throwable.message}")
        }

    init {
        // Dark Mode settings
        runBlocking(coroutineContext + coroutineExceptionHandler) {
            repository.isNightMode().first()
        }.also {
            Timber.d("init | isNightMode() | dark mode value: $it")
            updateDarkMode(it)
        }

        // Vibration settings
        runBlocking(coroutineContext + coroutineExceptionHandler) {
            repository.isVibration().first()
        }.also {
            Timber.d("init | isVibration() | value: $it")
            updateVibration(it)
        }

        // Activities splashscreen
        runBlocking(coroutineContext + coroutineExceptionHandler) {
            repository.isActivitiesSplashScreenEnabled().first()
        }.also {
            Timber.d("init | isActivitiesSplashScreenEnabled() | is enabled value: $it")
            updateActivitiesSplashEnabled(it)
        }

        getLoggedUser()
    }


    //////////////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////////////
    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnThemeSelected -> {
                if (event.option.contains("light", true)) {
                    updateDarkMode(false)
                    updateDarkModeDatastore()
                } else if (event.option.contains("dark", true)) {
                    updateDarkMode(true)
                    updateDarkModeDatastore()
                } else {
                    updateDarkMode(isDarkMode)
                }
            }

            is UiEvent.OnUpdateVibrationEnable -> {
                updateVibrationDatastore()
                updateVibration(event.isVibrationEnable)
            }

            is UiEvent.OnUpdateActivitiesSplashScreenEnable -> {
                updateActivitiesSplashScreenDatastore()
                updateActivitiesSplashEnabled(event.isActivitiesSplashScreenEnable)
            }

            is UiEvent.OnUpdateUser -> updateUser(event.user)
            is UiEvent.OnUpdateDeviceInfo -> updateDeviceInfo(event.deviceInformation)
            is UiEvent.OnUpdateShowMoreInfoOnDevice -> updateShowMoreInfoOnDevice(event.expanded)
            is UiEvent.OnLogoutClicked -> logout()
        }
    }

    private fun updateDarkModeDatastore() {
        Timber.d("updateDarkModeDatastore()")
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            repository.toggleNightMode()
        }
    }

    private fun updateVibrationDatastore() {
        Timber.d("updateVibrationDatastore()")
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            repository.toggleVibration()
        }
    }

    private fun updateActivitiesSplashScreenDatastore() {
        Timber.d("updateActivitiesSplashScreenDatastore()")
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            repository.toggleActivitiesSplashScreenEnabled()
        }
    }

    private fun getLoggedUser() {
        Timber.d("getLoggedUser()")

        val user: User? =
            runBlocking(coroutineContext + SupervisorJob() + coroutineExceptionHandler) {
                repository.getUsersSync().firstOrNull { it.logged }
            }
        user?.let { updateUser(it) }
    }

    private fun logout() {
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            repository.getUsersSync().firstOrNull { it.logged }?.let {
                repository.logoutUser(it._id.toInt())
            }
        }
    }

    fun fetchDeviceInformation() {
        Timber.d("getDeviceInfo()")

        viewModelScope.launch(coroutineContext) {
            //Retrieve Screen's height and width
            val metrics = DisplayMetrics()

            val mDeviceInfo = DeviceInformation(
                LabDeviceManager.getDevice().toString(),
                LabDeviceManager.getBrand().toString(),
                LabDeviceManager.getModel(),
                LabDeviceManager.getSerial().toString(),
                LabDeviceManager.getFingerPrint().toString(),
                LabDeviceManager.getHardware().toString(),
                LabDeviceManager.getSerial().toString(),
                LabDeviceManager.getID().toString(),
                metrics.widthPixels,
                metrics.heightPixels,
                LabCompatibilityManager.getOSName(),
                LabDeviceManager.getSdkVersion(),
                LabDeviceManager.getRelease().toString(),
                LabDeviceManager.isRooted()
            )

            delay(1_500)

            withContext(Dispatchers.Main) {
                updateDeviceInfo(mDeviceInfo)
            }
        }
    }
}