package com.riders.thelab.call.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import com.riders.thelab.call.core.utils.LabCallManager
import com.riders.thelab.call.ui.main.dialer.DialerScreen
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import timber.log.Timber

class TheLabCallMainActivity : BaseComponentActivity() {
    private val mPhonePermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Timber.e("mPhonePermissionRequestLauncher | Phone permission not granted")
        } else {
            Timber.i("mPhonePermissionRequestLauncher | Phone permission granted")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        LabCallManager.registerPhoneAccount(this)

        setContent {
            TheLabTheme(theme = AppTheme.Default) {
                DialerScreen()
            }
        }

        try {
            requestPhonePermission()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }


    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    override fun onDestroy() {
        Timber.e("onDestroy()")
        super.onDestroy()
    }


    @RequiresPermission(Manifest.permission.CALL_PHONE)
    fun requestPhonePermission() {
        Timber.d("requestPhonePermission() | Requesting phone permission")
        if (!LabCallManager.hasCallPhonePermission(this)) {
            Timber.i("requestPhonePermission() | App doesn't have phone permission")
            mPhonePermissionRequestLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }
}