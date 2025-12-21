package com.riders.thelab.central.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.data.utils.UiState
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainCentralActivity : BaseComponentActivity() {

    private val mViewModel: MainCentralViewModel by viewModels<MainCentralViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        mViewModel.initWeakReference(this@MainCentralActivity)

        computeWindowSizeClasses()

        enableEdgeToEdge()

        setContent {
            // Register lifecycle events
            mViewModel.observeLifecycleEvents(lifecycle = LocalLifecycleOwner.current.lifecycle)

            val theme: AppTheme by mViewModel
                .theme
                .collectAsStateWithLifecycle()
            val isDarkTheme: Boolean? by mViewModel
                .isDarkMode
                .collectAsStateWithLifecycle()

            val centralUiState: UiState<List<PackageApp>> by mViewModel.centralUiState.collectAsStateWithLifecycle()

            TheLabTheme(
                theme = theme,
                darkTheme = isDarkTheme?: isSystemInDarkTheme()
            ) {
                CentralScreen(
                    theme = theme,
                    darkTheme = isDarkTheme?: isSystemInDarkTheme(),
                    windowSize = getDeviceWindowsSizeClass(),
                    centralUiState = centralUiState,
                    searchModeEnabled = mViewModel.searchModeEnabled,
                    searchQuery = mViewModel.searchPackageQuery,
                    isHideBottomSheetContentRequested = mViewModel.isHideBottomSheetContentRequested,
                    uiEvent = mViewModel::onEvent
                )
            }
        }
    }

    override fun backPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
    }
}
