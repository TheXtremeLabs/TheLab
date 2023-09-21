package com.riders.thelab.ui.splashscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + Job()


    private val mViewModel: SplashScreenViewModel by viewModels()

    private val permissionRequestLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Timber.d("registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) | $it")

        }


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        if (LabCompatibilityManager.isTiramisu()) {
            requestPermissionForAndroid13()
        }

        mViewModel.retrieveAppVersion(this@SplashScreenActivity)
        mViewModel.getVideoPath(this@SplashScreenActivity)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SplashScreenContent(mViewModel)
                        }
                    }
                }
            }
        }
    }


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    fun requestPermissionForAndroid13() {
        Timber.d("requestPermissionForAndroid13()")

        if (ContextCompat.checkSelfPermission(
                this@SplashScreenActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Timber.i("All permissions are granted. Continue workflow")

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val packageName = this@SplashScreenActivity.packageName
                val uri: Uri = Uri.fromParts(packageName, packageName, null)
                data = uri
            }.run {
                startActivity(this)
            }
        } else {
            Timber.e("Launch permissionRequestLauncher")
            permissionRequestLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }
    }

    fun goToLoginActivity() {
        Timber.d("goToLoginActivity()")
        Navigator(this).callLoginActivity()
        finish()
    }
}