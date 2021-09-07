package com.riders.thelab.ui.biometric

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import co.infinum.goldfinger.Goldfinger
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabDeviceManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.databinding.ActivityBiometricBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.observers.DisposableObserver
import timber.log.Timber

@AndroidEntryPoint
class BiometricActivity : AppCompatActivity() {

    private lateinit var context: Context

    // Views
    private var _viewBinding: ActivityBiometricBinding? = null

    private val binding get() = _viewBinding!!

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        // initGoldFinger
        initGoldFinger()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }


    ///////////////////////
    //
    // CLASSES METHODS
    //
    ///////////////////////
    private fun init() {
        context = this

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.activity_title_biometric)

        binding.fingerPrintBtn.setOnClickListener {
            Timber.d("on fingerprint button clicked")

            if (!LabDeviceManager.getRxGoldFinger()?.canAuthenticate()!!) {
                Timber.e("Cannot authenticate")
            } else {
                authenticateWithRX()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun initGoldFinger() {
        LabDeviceManager.initFingerPrintWithRx(context, this)

        // Check if fingerprint hardware is available
        if (!LabDeviceManager.hasFingerPrintHardware()) {
            Timber.e("The device doesn't have finger print hardware")
            showSnackBar(
                "The device doesn't have finger print hardware", SnackBarType.ALERT,
                "LEAVE"
            ) { finish() }
            return
        }
        Timber.d("Fingerprint hardware ok")

        // Check if device can authenticate
        if (!LabDeviceManager.getRxGoldFinger()?.canAuthenticate()!!) {
            Timber.e("Cannot authenticate")
            showSnackBar(
                "Cannot authenticate",
                SnackBarType.ALERT,
                "QUIT"
            ) { finish() }
            return
        }

        // Init successfully executed
        showSnackBar(
            "Fingerprint initialization successfully executed",
            SnackBarType.NORMAL,
            "CONTINUE"
        ) { Timber.d("User action validate") }
    }

    private fun showSnackBar(
        message: String,
        type: SnackBarType,
        actionText: String,
        listener: View.OnClickListener
    ) {
        UIManager.showActionInSnackBar(this, binding.root, message, type, actionText, listener)
    }

    private fun authenticateWithRX() {
        Timber.d("authenticateWithRX()")

        //rxGoldFinger
        LabDeviceManager.getGoldFingerPromptParams()?.let {
            LabDeviceManager.getRxGoldFinger()
                ?.authenticate(it)
                ?.subscribe(object : DisposableObserver<Goldfinger.Result?>() {
                    override fun onComplete() {
                        /* Fingerprint authentication is finished */
                        Timber.d("Authentication complete")
                    }

                    override fun onError(e: Throwable) {
                        /* Critical error happened */
                        Timber.e(e)
                    }

                    override fun onNext(result: Goldfinger.Result) {
                        /* Result received */
                        Timber.d(
                            """
                            Result :
                            type : ${result.type()}
                            value : ${result.value()}
                            reason : ${result.reason()}
                            message : ${result.message()}
                            
                            """.trimIndent()
                        )
                    }
                })
        }
    }
}