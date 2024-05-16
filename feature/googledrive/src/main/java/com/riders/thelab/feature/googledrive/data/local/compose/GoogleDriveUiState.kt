package com.riders.thelab.feature.googledrive.data.local.compose

import kotools.types.text.NotBlankString

sealed interface GoogleDriveUiState {
    data object Loading : GoogleDriveUiState

    data class Error(val message: NotBlankString, val throwable: Throwable? = null) :
        GoogleDriveUiState

    data object GooglePlayServicesUnavailable : GoogleDriveUiState

    data object Success : GoogleDriveUiState
}
