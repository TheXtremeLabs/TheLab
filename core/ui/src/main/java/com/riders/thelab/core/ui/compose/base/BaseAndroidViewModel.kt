package com.riders.thelab.core.ui.compose.base

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.data.local.IUiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.lang.ref.WeakReference

open class BaseAndroidViewModel(
    application: Application,
    val uiRepository: IUiRepository
) : AndroidViewModel(application) {

    var mWeakReference: WeakReference<ComponentActivity>? = null
        private set

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    // --- Version
    private var _version: MutableStateFlow<String> = MutableStateFlow("")
    open var version: StateFlow<String> = _version

    // --- Is First Time Launched
    val isFirstTimeLaunched: StateFlow<Boolean> by lazy {
        uiRepository
            .getIsFirstTimeLaunched()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), true)
    }

    // --- Theme
    val theme: StateFlow<AppTheme> by lazy {
        uiRepository
            .getThemeColorAsAppTheme()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), AppTheme.Default)
    }

    // --- Dark Mode
    val isDarkMode: StateFlow<Boolean?> by lazy {
        uiRepository
            .isDarkTheme()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), null)
    }

    // --- Vibration
    val isVibration: StateFlow<Boolean> by lazy {
        uiRepository
            .getIsVibrationEnabled()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), true)
    }

    // --- Activities Splash
    val isActivitiesSplashEnabled: StateFlow<Boolean> by lazy {
        uiRepository
            .getIsActivitiesSplashEnabled()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), true)
    }

    //////////////////////////////////////////
    // Properties
    //////////////////////////////////////////
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

}