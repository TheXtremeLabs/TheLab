package com.riders.thelab.feature.mlkit.ui.compose.barcodescanner

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.mlkit.data.local.model.BarcodeField
import com.riders.thelab.feature.mlkit.ui.compose.base.BaseCameraActivity
import com.riders.thelab.feature.mlkit.ui.compose.utils.MLKitComposeNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BarcodeScannerActivity : BaseCameraActivity() {

    private lateinit var navigator: MLKitComposeNavigator

    private val mViewModel: BarcodeScannerViewModel by viewModels<BarcodeScannerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        navigator = MLKitComposeNavigator(this@BarcodeScannerActivity)

        // enableEdgeToEdge()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            BarcodeScannerContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart()")
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
    }

    override fun backPressed() {
        finish()
    }

    override fun onDestroy() {
        Timber.e("onDestroy()")
        super.onDestroy()
    }

    override fun onCameraPermissionGranted(granted: Boolean) {
        mViewModel.updateShowCamera(granted)
    }

    fun launchScanResultActivity(scanResult: BarcodeField) {
        navigator.launchBarcodeScanResultActivity(scanResult = scanResult)
    }
}