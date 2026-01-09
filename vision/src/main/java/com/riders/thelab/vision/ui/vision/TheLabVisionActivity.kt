package com.riders.thelab.vision.ui.vision

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.camera.CameraWorkflowModel
import com.riders.thelab.core.permissions.Permission
import com.riders.thelab.core.permissions.PermissionManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.vision.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import timber.log.Timber

@AndroidEntryPoint
class TheLabVisionActivity : BaseComponentActivity() {

    private val mCameraViewModel: CameraWorkflowModel by viewModels<CameraWorkflowModel>()
    private val mViewModel: VisionViewModel by viewModels<VisionViewModel>()

    private var mPermissionManager: PermissionManager? = null
    private var targetVision: NotBlankString? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        enableEdgeToEdge()

        mPermissionManager = PermissionManager.from(this)
    }

    override fun onResume() {
        super.onResume()

        mPermissionManager
            ?.request(Permission.Camera)
            ?.checkPermission { granted ->
                if (!granted) {
                    mPermissionManager?.shouldAskPermission(this, Permission.Camera.permissions[0])
                } else {
                    getBundle()

                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.CREATED) {

                            setContent {

                                // Register lifecycle events
                                mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                                val theme: AppTheme by mViewModel
                                    .theme
                                    .collectAsStateWithLifecycle()
                                val isDarkTheme: Boolean? by mViewModel
                                    .isDarkMode
                                    .collectAsStateWithLifecycle()

                                TheLabTheme(
                                    theme = theme,
                                    darkTheme = isDarkTheme ?: isSystemInDarkTheme()
                                ) {
                                    when (targetVision.toString()) {
                                        Constants.ML_KIT_VISION_BARCODE -> {
                                            BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                MLKitVisionScreen(
                                                    theme = theme,
                                                    isDarkTheme = isDarkTheme,
                                                    // Select back camera as a default
                                                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
                                                    imageAnalyzer = BarcodeScannerAnalyzer(
                                                        previewViewWidth = this.maxWidth.value,
                                                        previewViewHeight = this.maxHeight.value,
                                                        onBarcodeInfo = { barcodeInfo ->
                                                            Timber.e("onBarcodeInfo() | barcodeInfo : $barcodeInfo")
                                                        },
                                                        onBarcode = { barcodeField ->
                                                            Timber.e("onBarcode() | barcodeField : $barcodeField")
                                                        }
                                                    )
                                                )
                                            }
                                        }

                                        Constants.VISION_VIDEO -> VisionVideoScreen(
                                            theme = theme,
                                            isDarkTheme = isDarkTheme ?: isSystemInDarkTheme()
                                        )

                                        else -> VisionCameraScreen(
                                            theme = theme,
                                            isDarkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                            // Select back camera as a default
                                            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finishWithIntent(RESULT_CANCELED)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
    }

    private fun getBundle() {
        val bundle: Bundle? = intent.extras
        bundle?.let { extras ->
            targetVision = extras.getString(Constants.EXTRA_TARGET_VISION)
                ?.toNotBlankString()
                ?.getOrNull()
                ?.also { Timber.i("getBundle() | target vision : $it") }
        } ?: run {
            Timber.e("getBundle() | Bundle is null")
            UIManager.showToast(this, "Bundle is null")
        }
    }

    fun hasCameraPermission(): Boolean = run {
        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, CAMERA)
    }.also {
        Timber.d("hasCameraPermission() | result : $it")
    }

    private fun finishWithIntent(
        activityResult: Int,
        additionalMessage: NotBlankString? = null,
        errorMessage: NotBlankString? = null
    ) {
        val hasAdditionalMessage: Boolean =
            additionalMessage?.toString()?.trim()?.isNotBlank() ?: false
        val isError: Boolean = errorMessage?.toString()?.trim()?.isNotBlank() ?: false
        val finishIntent: Intent = Intent().apply {
            putExtra(
                Constants.EXTRA_ADDITIONAL_MESSAGE,
                if (!hasAdditionalMessage) null else additionalMessage.toString()
            )
            putExtra(Constants.EXTRA_ERROR_MESSAGE, if (!isError) null else errorMessage.toString())
        }
        Timber.i("finishWithIntent() | additionalMessage: ${additionalMessage.toString()}, errorMessage: ${errorMessage.toString()}")
        setResult(activityResult, finishIntent)
        finish()
    }
}
