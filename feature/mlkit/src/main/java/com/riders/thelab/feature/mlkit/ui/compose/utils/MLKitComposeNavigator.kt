package com.riders.thelab.feature.mlkit.ui.compose.utils

import android.content.Intent
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.feature.mlkit.data.local.model.BarcodeField
import com.riders.thelab.feature.mlkit.ui.compose.barcodescanner.BarcodeScannerActivity
import com.riders.thelab.feature.mlkit.ui.compose.barcodescanner.BarcodeScannerResultActivity
import com.riders.thelab.feature.mlkit.ui.compose.cameratest.CameraTestActivity
import com.riders.thelab.feature.mlkit.ui.compose.documentscanner.DocumentScannerActivity
import com.riders.thelab.feature.mlkit.ui.compose.face.FaceDetectionActivity

class MLKitComposeNavigator(private val activity: BaseComponentActivity) {


    fun launchCameraTestActivity() =
        Intent(activity, CameraTestActivity::class.java).run { startActivity(this) }

    fun launchBarcodeScanningActivity() =
        Intent(activity, BarcodeScannerActivity::class.java).run { startActivity(this) }

    fun launchBarcodeScanResultActivity(scanResult: BarcodeField) =
        Intent(activity, BarcodeScannerResultActivity::class.java).apply {
            this.putExtra(
                BarcodeScannerResultActivity.EXTRA_SCAN_RESULT,
                scanResult
            )
        }.run { startActivity(this) }

    fun launchDocumentScannerActivity() =
        Intent(activity, DocumentScannerActivity::class.java).run { startActivity(this) }

    fun launchInkRecognitionActivity() =
        Intent(activity, InkRecognitionActivity::class.java).run { startActivity(this) }

    fun launchFaceDetectionActivity() =
        Intent(activity, FaceDetectionActivity::class.java).run { startActivity(this) }

    fun launchTextRecognitionActivity() =
        Intent(activity, TextRecognitionActivity::class.java).run { startActivity(this) }

    fun launchTextRecognitionAssetsActivity() =
        Intent(activity, TextRecognitionAssetsActivity::class.java).run { startActivity(this) }

    fun launchTranslateActivity() =
        Intent(activity, TranslateActivity::class.java).run { startActivity(this) }

    private fun startActivity(intent: Intent) {
        activity.startActivity(intent)
    }
}