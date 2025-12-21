package com.riders.thelab.feature.settings.main

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
import com.riders.thelab.core.data.local.model.compose.settings.DeviceInfoUiState
import com.riders.thelab.core.data.local.model.compose.settings.UserUiState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.data.toThemeColorConfigProto
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.core.ui.data.local.preferences.proto.DarkThemeConfigProto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: IRepository,
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    val themeOptions: List<String> = listOf("Light", "Dark", "Use System")

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _preselectedDarkModeOption: MutableStateFlow<String> = MutableStateFlow("")
    val preselectedDarkModeOption: StateFlow<String> = _preselectedDarkModeOption

    private var _deviceInformationUiState: MutableStateFlow<DeviceInfoUiState> =
        MutableStateFlow(DeviceInfoUiState.Loading)
    var deviceInformationUiState: StateFlow<DeviceInfoUiState> = _deviceInformationUiState

    private var _userUiState: MutableStateFlow<UserUiState> =
        MutableStateFlow(UserUiState.Loading)
    var userUiState: StateFlow<UserUiState> = _userUiState

    var showMoreInfoOnDevice: Boolean by mutableStateOf(false)


    fun updatePreselectedDarkModeOption(option: String) {
        this._preselectedDarkModeOption.update { option }
    }

    private fun updateDeviceInfoUiState(newDeviceInformationState: DeviceInfoUiState) {
        this._deviceInformationUiState.value = newDeviceInformationState
    }

    private fun updateUserUiState(newUserState: UserUiState) {
        this._userUiState.value = newUserState
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
            Timber.e("Coroutine Exception caught with message: ${throwable.message} (${throwable.javaClass.canonicalName})")
        }


    //////////////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////////////
    fun onEvent(event: UiEvent) {
        Timber.d("onEvent() | event: $event")

        when (event) {
            is UiEvent.OnThemeSelected -> viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                uiRepository.updateThemeColor(event.newAppTheme.toThemeColorConfigProto())
            }

            is UiEvent.OnDarkModeSelected -> {
                updateDarkModeDatastore(event.option)
            }

            is UiEvent.OnUpdateVibrationEnable -> {
                updateVibrationDatastore(event.isVibrationEnable)
            }

            is UiEvent.OnUpdateActivitiesSplashScreenEnable -> {
                updateActivitiesSplashScreenDatastore(event.isActivitiesSplashScreenEnable)
            }

            is UiEvent.OnUpdateUser -> updateUserUiState(UserUiState.Success(event.user))
            is UiEvent.OnUpdateDeviceInfo -> updateDeviceInfoUiState(DeviceInfoUiState.Success(event.deviceInformation))
            is UiEvent.OnUpdateShowMoreInfoOnDevice -> updateShowMoreInfoOnDevice(event.expanded)
            is UiEvent.OnLogoutClicked -> logout()
        }
    }


    private fun updateDarkModeDatastore(option: String) {
        Timber.d("updateDarkModeDatastore()")

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if (option.contains("light", true)) {
                uiRepository
                    .updateThemeDarkConfig(darkThemeConfig = DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT)
                    .also { updatePreselectedDarkModeOption(option) }
            } else if (option.contains("dark", true)) {
                uiRepository
                    .updateThemeDarkConfig(darkThemeConfig = DarkThemeConfigProto.DARK_THEME_CONFIG_DARK)
                    .also { updatePreselectedDarkModeOption(option) }
            } else {
                uiRepository
                    .updateThemeDarkConfig(darkThemeConfig = DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM)
                    .also { updatePreselectedDarkModeOption(option) }
            }
        }
    }

    private fun updateVibrationDatastore(isVibration: Boolean) {
        Timber.d("updateVibrationDatastore()")
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            uiRepository.updateIsVibrationEnabled(isVibration)
        }
    }

    private fun updateActivitiesSplashScreenDatastore(isSplashScreenActivitiesEnabled: Boolean) {
        Timber.d("updateActivitiesSplashScreenDatastore()")
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            uiRepository.updateIsActivitiesSplashEnabled(isSplashScreenActivitiesEnabled)

        }
    }

    fun getLoggedUser() {
        val user: User? =
            runBlocking(coroutineContext + SupervisorJob() + coroutineExceptionHandler) {
                repository.getUsersSync().firstOrNull { it.logged }?.also {
                    Timber.d("getLoggedUser() | user: $it")
                }
            }
        user?.let {
            updateUserUiState(UserUiState.Success(it))
        } ?: run {
            updateUserUiState(UserUiState.NotConnected)
        }
    }

    private fun logout() {
        Timber.e("logout()")

        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            repository.getUsersSync().firstOrNull { it.logged }?.let {
                Timber.e("repository.getUsersSync() | user: $it")
                if (it.isGoogleAuth) {
                    it.isGoogleAuth = false

                    (mWeakReference?.get() as? SettingsActivity)?.signOut()
                }

                repository.logoutUser(it._id.toInt())
                updateUserUiState(UserUiState.NotConnected)
            } ?: run {
                Timber.e("repository.getUsersSync() | user is null")
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
                (mWeakReference?.get() as? SettingsActivity)?.getScreenWidth() ?: 0,
                (mWeakReference?.get() as? SettingsActivity)?.getScreenHeight() ?: 0,
                LabCompatibilityManager.getOSName(),
                LabDeviceManager.getSdkVersion(),
                LabDeviceManager.getRelease().toString(),
                LabDeviceManager.isRooted()
            )

            delay(1_500)

            withContext(Dispatchers.Main) {
                updateDeviceInfoUiState(DeviceInfoUiState.Success(mDeviceInfo))
            }
        }
    }
}