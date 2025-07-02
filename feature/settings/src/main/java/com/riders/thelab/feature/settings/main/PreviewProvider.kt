package com.riders.thelab.feature.settings.main

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.DeviceInformation
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.compose.settings.DeviceInfoUiState
import com.riders.thelab.core.data.local.model.compose.settings.UserUiState
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi

class PreviewProviderDeviceInfoUiState : PreviewParameterProvider<DeviceInfoUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<DeviceInfoUiState>
        get() = sequenceOf(
            DeviceInfoUiState.Loading,
            DeviceInfoUiState.Error(
                "Error occurred while getting value".toNotBlankString().getOrThrow()
            ),
            DeviceInfoUiState.Success(deviceInformation = DeviceInformation()),
        )
}

class PreviewProviderUserUiState : PreviewParameterProvider<UserUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<UserUiState>
        get() = sequenceOf(
            UserUiState.Loading,
            UserUiState.Error("Error occurred while getting value".toNotBlankString().getOrThrow()),
            UserUiState.Success(user = User.mockUserForTests[0]),
            UserUiState.NotConnected
        )
}