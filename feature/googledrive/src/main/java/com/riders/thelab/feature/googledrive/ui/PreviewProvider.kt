package com.riders.thelab.feature.googledrive.ui

import android.net.Uri
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.google.GoogleAccountModel
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleDriveUiState
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleSignInState
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import kotools.types.web.EmailAddress

class PreviewProviderUiState : PreviewParameterProvider<GoogleDriveUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<GoogleDriveUiState>
        get() = sequenceOf(
            GoogleDriveUiState.Loading,
            GoogleDriveUiState.Error(NotBlankString.create("Error occurred while getting value")),
            GoogleDriveUiState.GooglePlayServicesUnavailable,
            GoogleDriveUiState.Success
        )
}

class PreviewProviderGoogleSignInState : PreviewParameterProvider<GoogleSignInState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<GoogleSignInState>
        get() = sequenceOf(
            GoogleSignInState.Loading,
            GoogleSignInState.Connected(
                GoogleAccountModel(
                    emailAddress = EmailAddress.create("john.smith@test.com"),
                    idToken = "sdfkvbnimpsdbvSDFVj255edfvedfv754edfved4fv-edfb0defbmW",
                    firstName = NotBlankString.create("John"),
                    familyName = NotBlankString.create("Smith"),
                    displayName = NotBlankString.create("JohnSmith"),
                    phoneNumber = NotBlankString.create("+33658479632"),
                    profilePictureUri = NotBlankString.create(Uri.Builder().build().toString())
                )
            ),
            GoogleSignInState.Disconnected
        )
}