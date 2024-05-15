package com.riders.thelab.feature.googledrive.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.NoItemFound
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.googledrive.R
import com.riders.thelab.feature.googledrive.core.google.GoogleSignInManager
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleDriveUiState
import kotlinx.coroutines.launch

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun PlayServicesUnavailableContent() {
    val context = LocalContext.current
    val hue: Float by remember { mutableFloatStateOf(50f) }

    TheLabTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(.65f)
                    .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.max_card_image_height)),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_drive_logo),
                    contentDescription = null
                )

                Image(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_google_drive_logo),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = Color.DarkGray.copy(
                            alpha = 1f,
                            red = 253f,
                            blue = 150f,
                            green = hue
                        ),
                        blendMode = BlendMode.Saturation
                    )
                )
            }

            Text(
                text = "Unfortunately the Google Play Services are not available on your devices.\n\nYou cannot use this feature.",
                textAlign = TextAlign.Center
            )

            Button(
                onClick = { (context.findActivity() as GoogleDriveActivity).backPressed() },
                modifier = Modifier
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = null
                    )
                    Text(text = "Go Back")
                }
            }
        }
    }
}

@Composable
fun GoogleDriveContentSuccess(uiState: GoogleDriveUiState.Success, hasInternetConnection: Boolean) {

    val context = LocalContext.current
    val activity: GoogleDriveActivity = context.findActivity() as GoogleDriveActivity
    val scope = rememberCoroutineScope()
    val googleDriveHelper = uiState.googleDriveHelper

    /*val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Timber.d("Recomposition | ActivityResultContracts.StartActivityForResult | resultCode: ${if (result.resultCode == Activity.RESULT_OK) "Activity.RESULT_OK" else "Activity.RESULT_CANCELED"}, data: ${result.data}")

            val intent: Intent? = result.data

            when (result.resultCode) {
                Activity.RESULT_CANCELED -> {
                    Timber.e("Recomposition | ActivityResultContracts.StartActivityForResult | Activity.RESULT_CANCELED")

                    if (null == intent) {
                        UIManager.showToast(context, "Google Login Error!")
                        return@rememberLauncherForActivityResult
                    }
                    Timber.e("Recomposition | ActivityResultContracts.StartActivityForResult | Activity.RESULT_CANCELED | bundle: ${intent.extras?.toString()}")
                }

                Activity.RESULT_OK -> {
                    Timber.d("Recomposition | ActivityResultContracts.StartActivityForResult | Activity.RESULT_OK")

                    if (null == intent) {
                        UIManager.showToast(context, "Google Login Error!")
                        return@rememberLauncherForActivityResult
                    }
                    UIManager.showToast(context, "Google Login Success!")

                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)

                    */
    /**
     * handle [task] result
     *//*
                    Timber.d("Recomposition | ActivityResultContracts.StartActivityForResult | handle [task] result")

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
                                    googleDriveHelper.setGoogleAccount(account)
                                }
                            }
                        }
                }

                else -> {
                    Timber.d("Recomposition | ActivityResultContracts.StartActivityForResult | else branch with result code : ${result.resultCode}")
                }
            }
        }*/

    TheLabTheme {

        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                modifier = Modifier,
                onClick = {
                    /*if (googleDriveHelper.isServerClientIDValid(context)) {
                        googleDriveHelper.googleSignIn(activity.mGoogleSignInRequestLauncher)
                    }*/
                    scope.launch { GoogleSignInManager(activity).signIn() }

                    /*startForResult.launch(
                        uiState.googleDriveHelper.getGoogleSignInClient(context).signInIntent
                    )*/
                },
            ) {
                Text(text = "Sign in with Google")
            }
        }
    }

}


@Composable
fun GoogleDriveContent(
    uiState: GoogleDriveUiState,
    hasInternetConnection: Boolean,
    uiEvent: (UiEvent) -> Unit
) {

    TheLabTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TheLabTopAppBar(
                toolbarSize = ToolbarSize.SMALL,
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(8.dp),
                            painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.googleg_color),
                            contentDescription = "google_icon"
                        )
                    }
                },
                title = stringResource(id = com.riders.thelab.core.ui.R.string.activity_title_google_drive)
            )
        }) { contentPadding ->
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                targetState = uiState,
                contentAlignment = Alignment.Center,
                label = "youtube_like_animated_content"
            ) { targetState ->
                when (targetState) {
                    is GoogleDriveUiState.Loading -> {
                        LabLoader(modifier = Modifier.size(56.dp))
                    }

                    is GoogleDriveUiState.Error -> {
                        NoItemFound("An error occurred while loading\n${targetState.message}")
                    }

                    is GoogleDriveUiState.GooglePlayServicesUnavailable -> {
                        PlayServicesUnavailableContent()
                    }

                    is GoogleDriveUiState.Success -> {
                        GoogleDriveContentSuccess(
                            uiState = targetState,
                            hasInternetConnection = hasInternetConnection
                        )
                    }
                }
            }
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewPlayServicesUnavailableContent() {
    TheLabTheme {
        PlayServicesUnavailableContent()
    }
}

@DevicePreviews
@Composable
private fun PreviewGoogleDriveContentSuccess(@PreviewParameter(PreviewProviderUiState::class) state: GoogleDriveUiState) {
    if (state is GoogleDriveUiState.Success) {
        TheLabTheme {
            GoogleDriveContentSuccess(uiState = state, hasInternetConnection = true)
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewGoogleDriveContent(@PreviewParameter(PreviewProviderUiState::class) state: GoogleDriveUiState) {
    TheLabTheme {
        GoogleDriveContent(uiState = state, hasInternetConnection = true) {}
    }
}