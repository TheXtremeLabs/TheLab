package com.riders.thelab.ui.splashscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.riders.thelab.R
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel() {

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

    //////////////////////////////////////////
    //
    // Class Methods
    //
    //////////////////////////////////////////
    fun activitiesSplashScreenEnabled(): Boolean =
        runBlocking(Dispatchers.IO + coroutineExceptionHandler) {
            repository.isActivitiesSplashScreenEnabled().first()
        }

    fun checkUserLoggedIn(): Boolean = runBlocking(Dispatchers.IO + coroutineExceptionHandler) {
        repository.getUsersSync().isNotEmpty()
                && 1 == repository.getUsersSync().size
                && null != repository.getUsersSync().firstOrNull { it.logged }
    }

    fun getVideoPath(activity: SplashScreenActivity): String? = try {
        val videoPath =
            Constants.ANDROID_RES_PATH +
                    activity.packageName.toString() +
                    Constants.SEPARATOR +
                    //Smartphone portrait video or Tablet landscape video
                    if (!LabCompatibilityManager.isTablet(activity)) R.raw.splash_intro_testing_sound_2 else R.raw.splash_intro_testing_no_sound_tablet
        updateVideoPath(videoPath)
        videoPath
    } catch (exception: Exception) {
        exception.printStackTrace()
        Timber.e(exception.message)
        null
    }

    fun onEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnUpdateSwitchContent -> updateSwitchContent(uiEvent.isSwitchContent)
            is UiEvent.OnUpdateStartCountDown -> updateStartCountDown(uiEvent.isStarted)
        }
    }
}