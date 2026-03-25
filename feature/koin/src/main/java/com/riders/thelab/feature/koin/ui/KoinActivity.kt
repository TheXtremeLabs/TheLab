package com.riders.thelab.feature.koin.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.koin.di.KoinModule
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class KoinActivity : BaseComponentActivity() {

    ////////////////////////////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val koinApp = {
            startKoin {
                // Log Koin into Android logger
                androidLogger(level = Level.DEBUG)
                // Reference Android context
                androidContext(this@KoinActivity.applicationContext)
                // Load modules
                modules(KoinModule.appModule)
            }
        }

        Timber.d("onCreate() | ${KoinActivity::class.java.simpleName} successfully initialized")

        enableEdgeToEdge()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    KoinApplication(application = { koinApp.invoke() }) {
                        val mKoinViewModel: KoinViewModel = koinInject<KoinViewModel>()
                        TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                KoinMainContent(htmlContent = mKoinViewModel.htmlContent)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }
}