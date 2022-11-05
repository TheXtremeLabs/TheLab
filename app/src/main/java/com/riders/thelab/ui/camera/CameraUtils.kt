package com.riders.thelab.ui.camera

import androidx.camera.core.Camera

object CameraUtils {
    fun turnOnCameraFlash(camera: Camera) {
        camera.cameraControl.enableTorch(true)
    }

    fun turnOffCameraFlash(camera: Camera) {
        camera.cameraControl.enableTorch(false)
    }
}