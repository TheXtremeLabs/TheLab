package com.riders.thelab.ui.biometric

import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.TheLabTopAppBar
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.utils.findActivity
import com.riders.thelab.data.local.model.biometric.AuthError
import com.riders.thelab.data.local.model.compose.LoginUiState
import timber.log.Timber

@Composable
fun BiometricPromptContainer(
    viewModel: BiometricViewModel,
    state: BiometricPromptContainerState,
    onAuthSucceeded: (cryptoObject: CryptoObject?) -> Unit = {},
    onAuthError: (AuthError) -> Unit = {},
) {
    val callback = remember(state) {
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Timber.e("onAuthenticationError: $errorCode : $errString")
                state.resetShowFlag()
                onAuthError(AuthError(errorCode, errString.toString()))
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                Timber.d("onAuthenticationSucceeded")
                state.resetShowFlag()
                onAuthSucceeded(result.cryptoObject)
            }
        }
    }

    val showPrompt: Boolean by state.isPromptToShow
    if (showPrompt) {
        val activity = LocalContext.current.findActivity() as BiometricActivity
        LaunchedEffect(key1 = state.cryptoObject) {
            val prompt = BiometricPrompt(activity, viewModel.executor, callback)
            prompt.authenticate(state.promptInfo, state.cryptoObject)
        }
    }

}


@Composable
fun BiometricContent(viewModel: BiometricViewModel) {
    val uiState: LoginUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    // create the state
    val promptContainerState = rememberPromptContainerState()
    // we will see later this part. However crypto object should be provided via our view model
    val cryptoObject: CryptoObject = viewModel.getCryptoObject()
    // Define prompt info. Can also provided by viewModel or define in a remember block dependent to to our cryptoObject
    val info = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Enroll your fingerprint ?")
        //.setAllowedAuthenticators(BIOMETRIC_STRONG)
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Cancel")
        .build()

    (uiState as LoginUiState.Logged).authContext?.let { auth ->
        val resources = LocalContext.current.resources
        LaunchedEffect(key1 = auth) {
            val promptInfo = viewModel.createPromptInfo(auth.purpose, resources)
            promptContainerState.authenticate(promptInfo, auth.cryptoObject)
        }
    }

    TheLabTheme {
        Scaffold(
            topBar = { TheLabTopAppBar(title = stringResource(R.string.activity_title_biometric)) }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Add the @Composable contaier
                BiometricPromptContainer(
                    viewModel,
                    promptContainerState,
                    onAuthSucceeded = { cryptoObj ->
                        //notify your viewmodel about the succeeded auth (passing the cryptoObj)
                        viewModel.onAuthSucceeded(cryptoObject = cryptoObj)
                    },
                    onAuthError = { authErr ->
                        //notify your viewmodel about the succeeded auth (passing the cryptoObj)
                        viewModel.onAuthError(
                            errorCode = authErr.errorCode,
                            errString = authErr.errString
                        )
                    }
                )


                Button(onClick = {
                    // To show the prompt.
                    viewModel.doLogin()
                }) {

                }
            }
        }
    }

}

@DevicePreviews
@Composable
fun PreviewBiometricContent() {
    val viewModel: BiometricViewModel = hiltViewModel()
    TheLabTheme {
        BiometricContent(viewModel)
    }
}