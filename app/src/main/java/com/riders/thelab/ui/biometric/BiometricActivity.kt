package com.riders.thelab.ui.biometric

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import co.infinum.goldfinger.Goldfinger
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.LabDeviceManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.databinding.ActivityBiometricBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.observers.DisposableObserver
import timber.log.Timber

@AndroidEntryPoint
class BiometricActivity : AppCompatActivity() {

    private var context: Context? = null

    // Views
    private lateinit var viewBinding: ActivityBiometricBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBiometricBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        init()

        if (!LabCompatibilityManager.isMarshmallow()) {
            Timber.e("Incompatibility detected - Device runs on API below API 23 (Marshmallow)")
            UIManager
                    .showActionInSnackBar(
                            this,
                            viewBinding.root,
                            "Incompatibility detected - Device runs on API below API 23 (Marshmallow)",
                            SnackBarType.ALERT,
                            "LEAVE"
                    ) { v: View? -> finish() }
        } else {

            // initGoldFinger
            initGoldFinger()
        }
    }

    ///////////////////////
    //
    // CLASSES METHODS
    //
    ///////////////////////
    private fun init() {
        context = this

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(getString(R.string.activity_title_biometric))

        viewBinding.fingerPrintBtn.setOnClickListener {
            Timber.d("on fingerprint button clicked")

            if (!LabDeviceManager.getRxGoldFinger().canAuthenticate()) {
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
            UIManager
                    .showActionInSnackBar(
                            this,
                            findViewById(R.id.content),
                            "The device doesn't have finger print hardware",
                            SnackBarType.ALERT,
                            "LEAVE"
                    ) { v: View? -> finish() }
            return
        }
        Timber.d("Fingerprint hardware ok")

        // Check if device can authenticate
        if (!LabDeviceManager.getRxGoldFinger().canAuthenticate()) {
            Timber.e("Cannot authenticate")
            UIManager
                    .showActionInSnackBar(
                            this,
                            findViewById(R.id.content),
                            "Cannot authenticate",
                            SnackBarType.ALERT,
                            "QUIT"
                    ) { v: View? -> finish() }
            return
        }

        // Init successfully executed
        UIManager
                .showActionInSnackBar(
                        this,
                        findViewById(R.id.content),
                        "Fingerprint initialization successfully executed",
                        SnackBarType.NORMAL,
                        "CONTINUE"
                ) { v: View? -> Timber.d("User action validate") }
    }

    private fun authenticateWithRX() {
        Timber.d("authenticateWithRX()")

        //rxGoldFinger
        LabDeviceManager.getRxGoldFinger()
                .authenticate(LabDeviceManager.getGoldFingerPromptParams())
                .subscribe(object : DisposableObserver<Goldfinger.Result?>() {
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
                        
                        """.trimIndent())
                    }
                })
    }
}