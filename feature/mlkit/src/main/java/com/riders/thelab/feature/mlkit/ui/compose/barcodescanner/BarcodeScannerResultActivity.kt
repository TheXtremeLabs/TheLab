package com.riders.thelab.feature.mlkit.ui.compose.barcodescanner

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.data.local.UiRepository
import com.riders.thelab.feature.mlkit.data.local.model.BarcodeField
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BarcodeScannerResultActivity : BaseComponentActivity() {

    private var mScanResult: BarcodeField? = null

    @Inject
    lateinit var uiRepository: UiRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        getBundle()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    val theme: AppTheme by uiRepository
                        .getTheme()
                        .collectAsStateWithLifecycle(initialValue = AppTheme.Default)
                    val isDarkTheme: Boolean by uiRepository
                        .isThemeDarkMode()
                        .collectAsStateWithLifecycle(initialValue = false)

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Transparent
                        ) {
                            BarcodeScannerResultContent(
                                theme = theme,
                                darkTheme = isDarkTheme,
                                scanResult = mScanResult!!
                            )
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }


    @SuppressLint("NewApi")
    private fun getBundle() {
        mScanResult = if (!LabCompatibilityManager.isTiramisu()) {
            @Suppress("DEPRECATION")
            intent.extras?.getSerializable(EXTRA_SCAN_RESULT) as BarcodeField
        } else {
            intent.extras?.getSerializable(EXTRA_SCAN_RESULT, BarcodeField::class.java)
        }
    }

    companion object {
        const val EXTRA_SCAN_RESULT: String = "EXTRA_SCAN_RESULT"
    }
}