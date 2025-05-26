package com.riders.thelab.feature.mlkit.ui.compose.barcodescanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.SparseIntArray
import android.view.Surface
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions.ZoomCallback
import com.google.mlkit.vision.barcode.common.Barcode
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.feature.mlkit.ui.compose.base.BaseCameraActivity

class BarcodeScannerUtils(
    private val activity: BaseCameraActivity,
    private val zoomCallback: ZoomCallback
) {

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .enableAllPotentialBarcodes() // Optional
        .setZoomSuggestionOptions(
            ZoomSuggestionOptions.Builder(zoomCallback)
                .setMaxSupportedZoomRatio(maxSupportedZoomRatio)
                .build()
        ) // Optional
        .build()

    private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @SuppressLint("NewApi")
    @Throws(CameraAccessException::class)
    fun getRotationCompensation(cameraId: String, activity: Activity, isFrontFacing: Boolean): Int {

        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        val deviceRotation =
            if (LabCompatibilityManager.isR()) activity.display?.rotation else activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = deviceRotation?.let { ORIENTATIONS.get(it) } ?: 0

        // Get the device's sensor orientation.
        val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
            .getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360
        } else { // back-facing
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360
        }
        return rotationCompensation
    }

    companion object {
        val maxSupportedZoomRatio = 2f
    }
}