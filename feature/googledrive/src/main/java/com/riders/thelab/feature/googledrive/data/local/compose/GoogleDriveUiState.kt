package com.riders.thelab.feature.googledrive.data.local.compose

import com.riders.thelab.feature.googledrive.ui.GoogleDriveActivity
import com.riders.thelab.feature.googledrive.utils.DriveServiceHelper
import com.riders.thelab.feature.googledrive.utils.GoogleDriveHelper
import kotools.types.text.NotBlankString

sealed interface GoogleDriveUiState {
    data object Loading : GoogleDriveUiState

    data class Error(val message: NotBlankString, val throwable: Throwable? = null) :
        GoogleDriveUiState

    data object GooglePlayServicesUnavailable : GoogleDriveUiState

    data class Success(
        val googleDriveHelper: GoogleDriveHelper<GoogleDriveActivity>,
        val driveServiceHelper: DriveServiceHelper<GoogleDriveActivity>? = null
    ) : GoogleDriveUiState
}
