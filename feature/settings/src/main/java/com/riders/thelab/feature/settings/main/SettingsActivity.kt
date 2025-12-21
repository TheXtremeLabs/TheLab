package com.riders.thelab.feature.settings.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Insets
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.google.BaseGoogleActivity
import com.riders.thelab.core.google.GoogleSignInManager
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.settings.profile.UserProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * SettingsActivity is the main entry point for the settings feature.
 * It provides a user interface for managing app settings, such as theme, dark mode, vibration, and more.
 *
 * This activity extends [BaseGoogleActivity] to provide Google Sign-In functionality and
 * uses Hilt for dependency injection.
 */
@AndroidEntryPoint
class SettingsActivity : BaseGoogleActivity() {

    private val mViewModel: SettingsViewModel by viewModels()

    private var mWindowMetrics: WindowMetrics? = null
    private var mWindowInsets: WindowInsets? = null
    private var mInsets: Insets? = null
    private var mDisplayBounds: Rect? = null
    private var mDisplayMetrics: DisplayMetrics? = null


    //////////////////////////////////////////
    //
    // OVERRIDE
    //
    //////////////////////////////////////////
    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (LabCompatibilityManager.isR()) {
            mWindowMetrics = windowManager.currentWindowMetrics
            mWindowInsets = mWindowMetrics?.windowInsets
            mDisplayBounds = mWindowMetrics?.bounds
            val insets = mWindowInsets?.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
            )
        } else {
            mWindowInsets = window.decorView.rootWindowInsets
            mDisplayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(mDisplayMetrics)
        }

        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        initViewModel()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val version by mViewModel.version.collectAsStateWithLifecycle()
                    val preselectedDarkModeOption: String by mViewModel.preselectedDarkModeOption.collectAsStateWithLifecycle()
                    val isVibration by mViewModel.isVibration.collectAsStateWithLifecycle()
                    val isActivitiesSplashEnabled by mViewModel.isActivitiesSplashEnabled.collectAsStateWithLifecycle()

                    val deviceInformationUiState by mViewModel.deviceInformationUiState.collectAsStateWithLifecycle()
                    val userUiState by mViewModel.userUiState.collectAsStateWithLifecycle()

                    LaunchedEffect(Unit) {
                        mViewModel.updatePreselectedDarkModeOption(
                            if (null == isDarkTheme) {
                                mViewModel.themeOptions.first { option ->
                                    option.contains(
                                        "system",
                                        true
                                    )
                                }
                            } else {
                                when (isDarkTheme) {
                                    true -> mViewModel.themeOptions.first { option ->
                                        option.contains(
                                            "dark",
                                            true
                                        )
                                    }

                                    else -> mViewModel.themeOptions.first { option ->
                                        option.contains(
                                            "light",
                                            true
                                        )
                                    }
                                }
                            }
                        )
                    }


                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SettingsContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                deviceInformationUiState = deviceInformationUiState,
                                userUiState = userUiState,
                                themeOptions = mViewModel.themeOptions,
                                version = version,
                                preselectedDarkModeOption = preselectedDarkModeOption,
                                showModeInfo = mViewModel.showMoreInfoOnDevice,
                                isVibration = isVibration,
                                isActivitiesSplashEnabled = isActivitiesSplashEnabled,
                                uiEvent = mViewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Called after [onCreate] — or after [onRestart] when
     * the activity had been stopped, but is now again being displayed to the
     * user. It will be followed by [onResume].
     */
    override fun onStart() {
        super.onStart()

        mViewModel.getAppVersion()
        mViewModel.fetchDeviceInformation()
        mViewModel.getLoggedUser()
    }

    /**
     * Handles the back button press.
     * Overrides the default behavior to log the event and finish the activity.
     */
    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    /**
     * Perform any final cleanup before an activity is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
    }


    //////////////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////////////
    /**
     * Initializes the view model by setting a weak reference to the activity.
     */
    private fun initViewModel() {
        mViewModel.initWeakReference(activity = this)
    }

    /**
     * Returns the width of the screen in pixels.
     *
     * @return Screen width.
     */
    @SuppressLint("NewApi")
    fun getScreenWidth(): Int = if (LabCompatibilityManager.isAndroid10()) {
        mDisplayBounds?.width()?.minus(mInsets?.left ?: 0)?.minus(mInsets?.right ?: 0) ?: 0
    } else {
        mDisplayMetrics?.widthPixels ?: 0
    }

    /**
     * Returns the height of the screen in pixels.
     *
     * @return Screen height.
     */
    @SuppressLint("NewApi")
    fun getScreenHeight(): Int = if (LabCompatibilityManager.isAndroid10()) {
        mDisplayBounds?.width()?.minus(mInsets?.top ?: 0)?.minus(mInsets?.bottom ?: 0) ?: 0
    } else {
        mDisplayMetrics?.heightPixels ?: 0
    }


    /**
     * Launches the [UserProfileActivity] to allow the user to edit their profile.
     */
    fun launchEditProfileActivity() =
        Intent(this, UserProfileActivity::class.java).run { startActivity(this) }

    /**
     * Performs a sign-out operation using the [GoogleSignInManager].
     */
    fun signOut() {
        GoogleSignInManager
            .getInstance(this)
            .signOut(
                activity = this,
                onSuccess = { loggedOut -> Timber.i("signOut() | loggedOut: $loggedOut") },
                onFailure = { throwable -> Timber.e("signOut() | throwable: $throwable") }
            )
    }

    /**
     * Called when a Google account is successfully connected.
     *
     * @param account The connected Google account.
     */
    override fun onConnected(account: GoogleSignInAccount) {
        Timber.e("onConnected() | account: $account")
    }

    /**
     * Called when a Google account is disconnected.
     */
    override fun onDisconnected() {
        Timber.e("onDisconnected()")
    }
}