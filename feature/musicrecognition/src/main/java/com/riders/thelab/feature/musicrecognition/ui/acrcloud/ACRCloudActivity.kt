@file:Suppress(
    "ControlFlowWithEmptyBody", "ControlFlowWithEmptyBody", "ControlFlowWithEmptyBody",
    "ControlFlowWithEmptyBody", "ControlFlowWithEmptyBody", "ControlFlowWithEmptyBody",
    "ControlFlowWithEmptyBody", "ControlFlowWithEmptyBody"
)

package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.utils.LabCompatibilityManager
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
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ACRCloudActivity : BaseComponentActivity() {

    private val mViewModel: ACRCloudViewModel by viewModels()

    private var permissionLauncher: ActivityResultLauncher<String>? = null

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
            } else if (error is CouldNotFindSpotifyApp) {
                // Show button to download Spotify
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

        if (LabCompatibilityManager.isTiramisu()) {
            // init Post notifications
            initPostNotificationsForAndroid13()
        }

        initPermissionLauncher()

        mViewModel.initNetworkManager(this@ACRCloudActivity)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
//                            color = MaterialTheme.colorScheme.background
                            color = if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background
                        ) {
                            ACRCloudActivityContent(mViewModel)
                        }
                    }
                }
            }
        }

        initObservers()
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

    @Deprecated("DEPRECATED - Use registerActivityForResult")
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
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
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

    private fun initObservers() {
        Timber.d("initObservers()")
        if (null != mViewModel.mNetworkManager) {
            mViewModel.mNetworkManager.getConnectionState().observe(this) {
                if (!it) {
                    Timber.e("getConnectionState().observe | NOT connected: $it")
                } else {
                    mViewModel.mNetworkManager.updateIsConnected(true)
                    mViewModel.getSpotifyToken()
                }
            }
        }
    }

    companion object {
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        private const val REQUEST_CODE = 1_337
        private const val REDIRECT_URI = "yourcustomprotocol://callback"
    }
}