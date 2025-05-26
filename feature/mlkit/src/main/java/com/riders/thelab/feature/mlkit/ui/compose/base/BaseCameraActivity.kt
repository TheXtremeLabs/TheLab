package com.riders.thelab.feature.mlkit.ui.compose.base

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import timber.log.Timber

abstract class BaseCameraActivity : BaseComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        initPermissionLauncher()

        if (!hasCameraPermission()) {
            launchPermissionRequest(CAMERA)
        }
    }

    private fun initPermissionLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
                Timber.d("initPermissionLauncher() | result : $grantResults")

                val areAllGranted = grantResults.all { permission -> permission.value }

                if (!areAllGranted) {
                    Timber.e("initPermissionLauncher() | Camera Permission is NOT granted")
                } else {
                    Timber.d("initPermissionLauncher() | Camera Permission is granted")
                }
            }
    }

    fun hasCameraPermission(): Boolean = run {
        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, CAMERA)
    }.also {
        Timber.d("hasCameraPermission() | result : $it")
    }

    override fun backPressed() {}
}