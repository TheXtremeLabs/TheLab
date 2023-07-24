package com.riders.thelab.ui.biometric

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.LabDeviceManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.bean.SnackBarType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

/*
 * Source: https://medium.com/@en.mazzucchelli/biometric-authentication-jetpack-compose-146ee35e7039
 */
@AndroidEntryPoint
class BiometricActivity : FragmentActivity() {

    private val mViewModel: BiometricViewModel by viewModels()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if fingerprint hardware is available
        if (!LabDeviceManager.hasFingerPrintHardware(context = this)) {
            Timber.e("The device doesn't have finger print hardware")
            UIManager.showActionInToast(
                this@BiometricActivity,
                "The device doesn't have finger print hardware",
            )
            finish()
            return
        }

        Timber.d("Fingerprint hardware ok")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            BiometricContent(mViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


    ///////////////////////
    //
    // CLASSES METHODS
    //
    ///////////////////////
}
