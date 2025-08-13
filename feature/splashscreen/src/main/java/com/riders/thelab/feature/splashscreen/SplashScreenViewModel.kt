package com.riders.thelab.feature.splashscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.utils.Constants
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: IRepository,
    val uiRepository: IUiRepository
) : BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var startCountDown: Boolean by mutableStateOf(false)
    var switchContent: Boolean by mutableStateOf(false)

    var videoPath: String? by mutableStateOf(null)
        private set


    private fun updateStartCountDown(started: Boolean) {
        startCountDown = started
    }

    private fun updateVideoPath(path: String) {
        this.videoPath = path
    }

    private fun updateSwitchContent(switch: Boolean) {
        this.switchContent = switch
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | ${throwable.message}")
        }

    ////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////
    init {
        runBlocking(coroutineContext + coroutineExceptionHandler) {

            // First time launched
            repository
                .isFirstTimeLaunched()
                .first()
                .also { isFirstTime ->
                    Timber.d("init | isFirstTimeLaunched() | is enabled value: $isFirstTime")

                    if (isFirstTime) {
                        repository
                            .saveFirstTimeLaunched(false)
                            .also { updateFirstTimeLaunched(false) }
                    } else {
                        Timber.v("init | isFirstTimeLaunched() | application has already been launched once")
                    }
                }

            // Vibration
            repository
                .isVibration()
                .first()
                .also {
                    Timber.d("init | isVibration() | is enabled value: $it")
                    updateVibration(it)
                }

            // Activities Splashscreen
            repository
                .isActivitiesSplashScreenEnabled()
                .first()
                .also {
                    Timber.d("init | isActivitiesSplashScreenEnabled() | is enabled value: $it")
                    updateActivitiesSplashEnabled(it)
                }

            // Activities Splashscreen
            repository
                .isActivitiesSplashScreenEnabled()
                .first()
                .also {
                    Timber.d("init | isActivitiesSplashScreenEnabled() | is enabled value: $it")
                    updateActivitiesSplashEnabled(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    //////////////////////////////////////////
    //
    // Class Methods
    //
    //////////////////////////////////////////
    fun getVideoPath(activity: SplashScreenActivity): String? = try {
        val videoPath =
            Constants.ANDROID_RES_PATH +
                    activity.packageName.toString() +
                    Constants.SEPARATOR +
                    //Smartphone portrait video or Tablet landscape video
                    if (!LabCompatibilityManager.isTablet(activity)) com.riders.thelab.core.ui.R.raw.splash_intro_testing_sound_2 else com.riders.thelab.core.ui.R.raw.splash_intro_testing_no_sound_tablet
        updateVideoPath(videoPath)
        videoPath
    } catch (exception: Exception) {
        exception.printStackTrace()
        Timber.e("getVideoPath() | onFailure | error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        activity.goToMainActivity(withError = true, throwable = exception)
        null
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnUpdateSwitchContent -> updateSwitchContent(event.isSwitchContent)
            is UiEvent.OnUpdateStartCountDown -> startCountDown(activity = event.activity)
        }
    }

    fun startCountDown(activity: SplashScreenActivity) {
        viewModelScope.launch {
            withTimeout(2_500L) { activity.goToMainActivity() }
        }
    }
}