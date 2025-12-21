package com.riders.thelab.feature.mlkit.ui.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.riders.thelab.core.common.utils.Constants
import com.riders.thelab.core.common.utils.LabPackageManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MLKitComposeMainActivity : BaseComponentActivity() {

    private val mViewModel: MLKitComposeMainViewModel by viewModels<MLKitComposeMainViewModel>()

    private val cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Timber.d("cameraLauncher | result : $result")

        when (result.resultCode) {
            RESULT_CANCELED -> {
                val reason: String? = result.data?.getStringExtra("ERROR_MESSAGE")
                Timber.e("cameraLauncher | error caught: $reason")
                UIManager.showToast(this, "Activity Result Canceled : $reason")
            }

            RESULT_OK -> {
                Timber.e("cameraLauncher | Activity Result OK From Camera")
                UIManager.showToast(this, "Activity Result OK From Camera")
            }
        }
    }

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.initNavigator(this@MLKitComposeMainActivity)

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
                            MLKitComposeMainContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                list = mViewModel.mlKitItems,
                                onSwap = { from, to -> mViewModel.swap(from, to) },
                            ) { event ->
                                when (event) {
                                    is UiEvent.None -> LabPackageManager
                                        .getInstance(context = this@MLKitComposeMainActivity)
                                        .callIntentForPackageActivity(
                                            activityResultLauncher = cameraLauncher,
                                            intentPackageName = Constants.PACKAGE_NAME_THE_LAB_VISION,
                                            Constants.EXTRA_TARGET_VISION to Constants.VISION_CAMERA
                                        )

                                    else -> mViewModel.onEvent(event)
                                }
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