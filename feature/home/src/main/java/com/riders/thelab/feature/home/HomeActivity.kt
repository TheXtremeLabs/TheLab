package com.riders.thelab.feature.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : BaseComponentActivity() {

    private val mViewModel: HomeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        mViewModel.initWeakReference(this)

        enableEdgeToEdge()

        computeWindowSizeClasses()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                Timber.d("onCreate() | repeatOnLifecycle(Lifecycle.State.CREATED)")

                setContent {
                    // Register lifecycle events
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val theme: AppTheme by mViewModel.uiRepository
                        .getTheme()
                        .collectAsStateWithLifecycle(initialValue = AppTheme.Default)
                    val isDarkTheme: Boolean by mViewModel.uiRepository
                        .isThemeDarkMode()
                        .collectAsStateWithLifecycle(initialValue = false)

                    val appList by mViewModel.appList.collectAsStateWithLifecycle()

                    if (isTv) {
                        HomeScreenTV(
                            theme = theme,
                            darkTheme = true,
                            whatsNewList = appList.take(3)
                        )
                    } else {
                        HomeScreen(
                            theme = theme,
                            darkTheme = isDarkTheme,
                            windowSize = getDeviceWindowsSizeClass()
                        )
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }

    fun launchApp(app: App) {
        Intent(this, app.appActivity).runCatching {
            Timber.w("launchApp() | Attempting lo launch ${app.appActivity?.simpleName}...")
            startActivity(this)
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("launchApp | onFailure | Error caught: ${it.message} (class : ${it.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.d("launchApp | onSuccess | Activity launched successfully")
                finish()
            }
    }
}