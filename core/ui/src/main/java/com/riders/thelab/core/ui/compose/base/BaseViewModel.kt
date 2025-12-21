package com.riders.thelab.core.ui.compose.base

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.data.local.IUiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import timber.log.Timber
import java.lang.ref.WeakReference

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
/*
 * To do that we will write an extension Composable function for ViewModel which
 * will receive Composable lifecycle Owner LocalLifecycleOwner.current.lifecycle
 * and will add observer and remove observer on onDispose block.
 *
 * The ViewModel will implement DefaultLifecycleObserver and will start receiving lifecycle events.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <viewModel : LifecycleObserver> viewModel.observeLifecycleEvents(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@observeLifecycleEvents)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycleEvents)
        }
    }
}

@Suppress("EmptyMethod")
open class BaseViewModel(val uiRepository: IUiRepository) : ViewModel() {

    var mWeakReference: WeakReference<ComponentActivity>? = null
        private set

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    // --- Version
    private var _version: MutableStateFlow<String> = MutableStateFlow("")
    open var version: StateFlow<String> = _version

    // --- Is First Time Launched
    private var _isFirstTimeLaunched: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isFirstTimeLaunched: StateFlow<Boolean> by lazy {
        uiRepository
            .getIsFirstTimeLaunched()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), true)
    }

    // --- Theme
    private var _theme: MutableStateFlow<AppTheme> = MutableStateFlow(AppTheme.Default)
    val theme: StateFlow<AppTheme> by lazy {
        uiRepository
            .getThemeColorAsAppTheme()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), AppTheme.Default)
    }

    // --- Dark Mode
    private var _isDarkMode: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isDarkMode: StateFlow<Boolean?> by lazy {
        uiRepository
            .isDarkTheme()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), null)
    }

    // --- Vibration
    private var _isVibration: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isVibration: StateFlow<Boolean> by lazy {
        uiRepository
            .getIsVibrationEnabled()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), true)
    }

    // --- Activities Splash
    private var _isActivitiesSplashEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isActivitiesSplashEnabled: StateFlow<Boolean> by lazy {
        uiRepository
            .getIsActivitiesSplashEnabled()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), true)
    }

    //////////////////////////////////////////
    // Properties

    private var isUserLoggedIn: Boolean by mutableStateOf(false)
        private set
    var viewPagerDotExpanded: Boolean by mutableStateOf(true)
        private set
    var viewPagerDotVisibility: Boolean by mutableStateOf(true)
        private set
    private var viewPagerCurrentIndex: Int by mutableIntStateOf(0)
        private set


    fun updateIsUserLoggedIn(isUserLoggedIn: Boolean) {
        Timber.d("updateIsUserLoggedIn() | isUserLoggedIn: $isUserLoggedIn")
        this.isUserLoggedIn = isUserLoggedIn
    }

    private fun updateVersion(appVersion: String) {
        Timber.d("updateVersion() | appVersion: $appVersion")
        this._version.update { appVersion }
    }

    fun updateIsFirstTimeLaunched(isFirstTimeLaunched: Boolean) {
        Timber.d("updateIsFirstTimeLaunched() | isFirstTimeLaunched: $isFirstTimeLaunched")
        this._isFirstTimeLaunched.update { isFirstTimeLaunched }
    }

    fun updateTheme(theme: AppTheme) {
        Timber.d("updateTheme() | theme: $theme")
        this._theme.update { theme }
    }

    fun updateIsDarkMode(isDarkMode: Boolean) {
        Timber.d("updateIsDarkMode() | isDarkMode: $isDarkMode")
        this._isDarkMode.update { isDarkMode }
    }

    fun updateIsVibration(isVibration: Boolean) {
        Timber.d("updateIsVibration() | isVibration: $isVibration")
        this._isVibration.update { isVibration }
    }

    fun updateIsActivitiesSplashEnabled(isActivitiesSplashEnabled: Boolean) {
        Timber.d("updateIsActivitiesSplashEnabled() | isActivitiesSplashEnabled: $isActivitiesSplashEnabled")
        this._isActivitiesSplashEnabled.update { isActivitiesSplashEnabled }
    }

    fun updateViewPagerExpanded(expanded: Boolean) {
        this.viewPagerDotExpanded = expanded
    }

    fun updateViewPagerDotVisibility(visible: Boolean) {
        this.viewPagerDotVisibility = visible
    }

    fun onCurrentPageChanged(pageChangedIndex: Int) {
        this.viewPagerCurrentIndex = pageChangedIndex
    }

    //////////////////////////////////////////
    // Class Methods
    //////////////////////////////////////////
    fun initWeakReference(activity: ComponentActivity) {
        if (null == mWeakReference) {
            mWeakReference = WeakReference(activity)
        }
    }

    @Throws(Exception::class)
    fun getAppVersion(): NotBlankString? {
        if (null == mWeakReference) {
            Timber.e("getAppVersion() | WeakReference is null")
            return null
        }

        if (version.value.trim().isNotBlank()) {
            return version.value.toNotBlankString().getOrThrow()
        }

        return mWeakReference?.get()?.let { activity ->
            runCatching {
                Timber.d("getAppVersion()")
                val pInfo: PackageInfo = activity
                    .packageManager
                    .getPackageInfo(activity.packageName, 0)
                return@runCatching pInfo.versionName?.toNotBlankString()?.getOrThrow()
                    ?.also { version ->
                        updateVersion(version.toString())
                    }
                    ?: throw Exception("Version is null")
            }
                .onFailure { exception ->
                    Timber.e("getAppVersion() | Error caught with message: ${exception.message} (${exception.javaClass.canonicalName})")
                }
                .onSuccess { version ->
                    Timber.d("getAppVersion() | version: $version")
                }
                .getOrThrow()
        } ?: run {
            Timber.e("getAppVersion() | WeakReference is null")
            return null
        }
    }
}
