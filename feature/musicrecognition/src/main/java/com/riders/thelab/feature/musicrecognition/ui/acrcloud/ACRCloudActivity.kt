package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.launch
import timber.log.Timber

class ACRCloudActivity : ComponentActivity() {

    private val mViewModel: ACRCloudViewModel by viewModels()

    private var permissionLauncher: ActivityResultLauncher<String>? = null


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (LabCompatibilityManager.isTiramisu()) {
            // init Post notifications
            initPostNotificationsForAndroid13()

            // Handle onBackPressed for Android 13+
            onBackInvokedDispatcher
                .registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                    Timber.e("Android 13+ onBackInvokedDispatcher | OnBackInvokedDispatcher.registerOnBackInvokedCallback()")
                    backPressed()
                }
        } else {
            onBackPressedDispatcher
                .addCallback(
                    this,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            Timber.e("Android 13- onBackPressedDispatcher | OnBackPressedCallback.handleOnBackPressed() | finish()")
                            // Back is pressed... Finishing the activity
                            backPressed()
                        }
                    })
        }

        initPermissionLauncher()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            ACRCloudActivityContent(mViewModel)
                        }
                    }
                }
            }
        }

        // checkPermission()
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    private fun initPermissionLauncher() {
        Timber.d("initPermissionLauncher()")
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (!isGranted) {
                    Timber.e("Record audio permission is NOT granted")
                } else {
                    Timber.d("Record audio permission is granted ini ACR variables")
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initPostNotificationsForAndroid13() {
        Timber.d("initPostNotificationsForAndroid13()")
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launchPermissionRequest(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            Timber.d("POST_NOTIFICATIONS Permission granted")
        }
    }

    private fun checkPermission() {
        Timber.d("checkPermission()")
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.e("RECORD_AUDIO Permission NOT granted")
            launchPermissionRequest(Manifest.permission.RECORD_AUDIO)
        } else {
            Timber.d("RECORD_AUDIO Permission granted")
            mViewModel.initACRCloud(this@ACRCloudActivity)
        }
    }

    private fun launchPermissionRequest(permission: String) {
        Timber.e("requestPermission() | permission: $permission")
        permissionLauncher?.launch(permission) ?: {
            Timber.e("Permission launcher has NOT been initialized")
        }
    }

    private fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }
}