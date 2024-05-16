package com.riders.thelab.feature.googledrive.ui

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.tasks.Task
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.googledrive.core.google.GooglePlayServicesManager
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleDriveUiState
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleSignInState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class GoogleDriveActivity : BaseComponentActivity(), OnConnectionFailedListener {

    private val mViewModel: GoogleDriveViewModel by viewModels<GoogleDriveViewModel>()

    private val mGoogleApiAvailability: GoogleApiAvailability by lazy {
        GoogleApiAvailability.getInstance()
    }

    val mGoogleSignInRequestLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Timber.d("Recomposition | ActivityResultContracts.StartActivityForResult | resultCode: ${if (result.resultCode == Activity.RESULT_OK) "Activity.RESULT_OK" else "Activity.RESULT_CANCELED"}, data: ${result.data}")

            val intent: Intent? = result.data

            when (result.resultCode) {
                RESULT_CANCELED -> {
                    Timber.e("ActivityResultContracts.StartActivityForResult | Activity.RESULT_CANCELED")

                    if (null == intent) {
                        UIManager.showToast(this@GoogleDriveActivity, "Google Login Error!")
                        return@registerForActivityResult
                    }
                    Timber.e("ActivityResultContracts.StartActivityForResult | Activity.RESULT_CANCELED | bundle: ${intent.extras?.toString()}")
                }

                RESULT_OK -> {
                    Timber.d("ActivityResultContracts.StartActivityForResult | Activity.RESULT_OK")

                    if (null == intent) {
                        UIManager.showToast(this@GoogleDriveActivity, "Google Login Error!")
                        return@registerForActivityResult
                    }
                    UIManager.showToast(this@GoogleDriveActivity, "Google Login Success!")

                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)

                    /**
                     * handle [task] result
                     */
                    Timber.d("ActivityResultContracts.StartActivityForResult | handle [task] result")

                    task
                        .addOnFailureListener { throwable ->
                            Timber.e("task | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
                        }
                        .addOnSuccessListener {
                            Timber.d("task | addOnSuccessListener | value: ${it.toString()}")
                        }
                        .addOnCompleteListener {
                            if (!task.isSuccessful) {
                                Timber.e("task | addOnCompleteListener | Google Sign In Failed")
                            } else {
                                Timber.i("task | addOnCompleteListener | Sign in successful")
                                val account = task.result

                                if (account != null) {
                                    mViewModel.mGoogleDriveHelper?.setGoogleAccount(account)
                                }
                            }
                        }
                }

                else -> {
                    Timber.d("ActivityResultContracts.StartActivityForResult | else branch with result code : ${result.resultCode}")
                }
            }
        }

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.initNetworkManager(
            activity = this@GoogleDriveActivity,
            lifecycle = this.lifecycle
        )

        if (!GooglePlayServicesManager.checkPlayServices(
                activity = this@GoogleDriveActivity,
                googleApiAvailability = mGoogleApiAvailability
            )
        ) {
            mViewModel.updateGoogleDriveUiState(GoogleDriveUiState.GooglePlayServicesUnavailable)
        } else {
            mViewModel.initHelpers(activity = this@GoogleDriveActivity)
        }

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    val uiState: GoogleDriveUiState by mViewModel.googleDriveUiState.collectAsStateWithLifecycle()
                    val signInState: GoogleSignInState by mViewModel.signInState.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            GoogleDriveContent(
                                uiState = uiState,
                                signInState = signInState,
                                hasInternetConnection = mViewModel.isConnected,
                                googleDriveHelper = mViewModel.mGoogleDriveHelper,
                                driveServiceHelper = mViewModel.mDriveServiceHelper,
                            ) { event -> mViewModel.onEvent(event) }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (null != mViewModel.mDriveServiceHelper) {
            mViewModel.mDriveServiceHelper?.queryFiles()
                ?.addOnFailureListener { throwable ->
                    Timber.e("task | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
                }
                ?.addOnSuccessListener {
                    Timber.d("task | addOnSuccessListener | value: ${it.toString()}")
                }
                ?.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Timber.e("task | addOnCompleteListener | failed")
                    } else {
                        Timber.i("task | addOnCompleteListener | successful, result: ${task.result}")
                    }
                }
        }
    }

    override fun backPressed() {
        finish()
    }

    fun launchGoogleSignIn(intent: Intent) {
        Timber.e("launchGoogleSignIn()")

        mGoogleSignInRequestLauncher.launch(intent)
    }


    override fun onConnectionFailed(result: ConnectionResult) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // Called whenever the API client fails to connect.
        Timber.e("onConnectionFailed() | GoogleApiClient connection failed: %s", result.toString())

        if (!result.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.errorCode, 0)
                ?.show()
            return
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */
        try {

            //result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

        } catch (e: IntentSender.SendIntentException) {

            e.printStackTrace()
            Timber.e("Exception while starting resolution activity")
        }
    }
}