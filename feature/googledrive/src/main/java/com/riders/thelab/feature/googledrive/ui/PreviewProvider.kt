package com.riders.thelab.feature.googledrive.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.feature.googledrive.data.local.compose.GoogleDriveUiState
import com.riders.thelab.feature.googledrive.utils.GoogleDriveHelper
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

class PreviewProviderUiState : PreviewParameterProvider<GoogleDriveUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<GoogleDriveUiState>
        get() = sequenceOf(
            GoogleDriveUiState.Success(GoogleDriveHelper(GoogleDriveActivity())),
            GoogleDriveUiState.Error(NotBlankString.create("Error occurred while getting value")),
            GoogleDriveUiState.GooglePlayServicesUnavailable,
            GoogleDriveUiState.Loading
        )
}