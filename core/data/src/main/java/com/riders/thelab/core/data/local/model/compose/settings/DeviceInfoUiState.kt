package com.riders.thelab.core.data.local.model.compose.settings

import com.riders.thelab.core.data.local.model.DeviceInformation
import com.riders.thelab.core.data.local.model.youtube.Video
import kotools.types.text.NotBlankString

sealed interface DeviceInfoUiState {
    data class Success(val deviceInformation: DeviceInformation) : DeviceInfoUiState
    data class Error(val message: NotBlankString, val throwable: Throwable? = null) : DeviceInfoUiState
    data object Loading : DeviceInfoUiState
}