package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp
import com.spotify.android.appremote.api.error.NotLoggedInException
import com.spotify.android.appremote.api.error.UserNotAuthorizedException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ACRCloudActivity : BaseComponentActivity() {

    private val mViewModel: ACRCloudViewModel by viewModels()

    override var permissionLauncher: ActivityResultLauncher<Array<String>>?
        get() = super.permissionLauncher
        set(value) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { areGranted ->
                if (!areGranted.values.all { it }) {
                    Timber.e("Record audio permission is NOT granted")
                } else {
                    Timber.d("Record audio permission is granted ini ACR variables")
                }
            }
        }

    private var mLabNetworkManager: LabNetworkManager? = null

    private val clientId = "1714852f79e04b24afd8a49d04068558"
    private val redirectUri = "http://com.yourdomain.yourapp/callback"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    private val mConnectionParams: ConnectionParams = ConnectionParams.Builder(clientId)
        .apply {
            setRedirectUri(REDIRECT_URI)
        }
        .build()

    private var mConnectionListener: ConnectionListener = object : ConnectionListener {
        override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
            mSpotifyAppRemote = spotifyAppRemote
            // setup all the things
        }

        override fun onFailure(error: Throwable?) {
            if (error is NotLoggedInException || error is UserNotAuthorizedException) {
                // Show login button and trigger the login flow from auth library when clicked
                Timber.d("Show login button and trigger the login flow from auth library when clicked")
            } else if (error is CouldNotFindSpotifyApp) {
                // Show button to download Spotify
                Timber.d("Show button to download Spotify")
            } else {
                Timber.e("onFailure: $error")
            }
        }
    }

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLabNetworkManager = LabNetworkManager
            .getInstance(this, lifecycle)
            .also { mViewModel.observeNetworkState(it) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {

                    // Register lifecycle events
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val acrUiState by mViewModel.uiState.collectAsStateWithLifecycle()
                    val mNetworkState by mViewModel.mNetworkState.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
//                            color = MaterialTheme.colorScheme.background
                            color = if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background
                        ) {
                            ACRCloudActivityContent(
                                acrUiState = acrUiState,
                                networkState = mNetworkState,
                                result = mViewModel.result ?: "",
                                canLaunchAudioRecognition = mViewModel.canLaunchAudioRecognition,
                                onStartRecognition = {
                                    if (!hasAudioPermission()) {
                                        launchPermissionRequest(Manifest.permission.RECORD_AUDIO)
                                    } else {
                                        mViewModel::startRecognition

                                    }
                                },
                                isRecognizing = mViewModel.isRecognizing
                            )
                        }
                    }
                }
            }
        }

        // checkPermission()

        /*val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)*/
    }

    /*private fun connected() {
        // Then we will write some more code here.
    }*/

    /*@Deprecated("DEPRECATED - Use registerActivityForResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {}
                AuthorizationResponse.Type.ERROR -> {}
                else -> {}
            }
        }
    }*/

    override fun onResume() {
        super.onResume()
        if (hasAudioPermission()) {
            mViewModel.initACRCloud(this@ACRCloudActivity)
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
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
    private fun hasAudioPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.e("hasAudioPermission() | RECORD_AUDIO Permission NOT granted")
            false
        } else {
            Timber.d("hasAudioPermission() | RECORD_AUDIO Permission granted")
            true
        }
    }


    companion object {
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        private const val REQUEST_CODE = 1_337
        private const val REDIRECT_URI = "yourcustomprotocol://callback"
    }
}