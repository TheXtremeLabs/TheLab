package com.riders.thelab.feature.googledrive.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.google.GoogleAccountModel
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleDriveUiState
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleSignInState
import kotools.types.text.toNotBlankString
import org.kotools.types.EmailAddress
import org.kotools.types.ExperimentalKotoolsTypesApi

class PreviewProviderUiState : PreviewParameterProvider<GoogleDriveUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<GoogleDriveUiState>
        get() = sequenceOf(
            GoogleDriveUiState.Loading,
            GoogleDriveUiState.Error(
                "Error occurred while getting value".toNotBlankString().getOrThrow()
            ),
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
                    emailAddress = EmailAddress.orThrow("john.smith@test.com"),
                    idToken = "sdfkvbnimpsdbvSDFVj255edfvedfv754edfved4fv-edfb0defbmW"
                        .toNotBlankString()
                        .getOrThrow(),
                    firstName = "John".toNotBlankString().getOrThrow(),
                    familyName = "Smith".toNotBlankString().getOrThrow(),
                    displayName = "JohnSmith".toNotBlankString().getOrThrow(),
                    phoneNumber = "+33658479632".toNotBlankString().getOrThrow(),
                    profilePictureUri = "N/A".toNotBlankString().getOrThrow()
                )
            ),
            GoogleSignInState.Disconnected
        )
}