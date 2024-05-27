package com.riders.thelab.ui.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.compose.LoginFieldsUIState
import com.riders.thelab.core.data.local.model.compose.LoginUiState

class PreviewProviderLoginState : PreviewParameterProvider<LoginUiState> {
    override val values: Sequence<LoginUiState>
        get() = sequenceOf(
            LoginUiState.UserSuccess(User.mockUserForTests[0]),
            LoginUiState.UserError(),
            LoginUiState.Connecting,
            LoginUiState.None
        )
}

class PreviewProviderLoginFieldsUIState : PreviewParameterProvider<LoginFieldsUIState.Login> {
    override val values: Sequence<LoginFieldsUIState.Login>
        get() = sequenceOf(
            LoginFieldsUIState.Login.Ok,
            LoginFieldsUIState.Login.HasError(""),
            LoginFieldsUIState.Login.Idle
        )
}

class PreviewProviderPasswordFieldsUIState : PreviewParameterProvider<LoginFieldsUIState.Password> {
    override val values: Sequence<LoginFieldsUIState.Password>
        get() = sequenceOf(
            LoginFieldsUIState.Password.Ok,
            LoginFieldsUIState.Password.HasError(""),
            LoginFieldsUIState.Password.Idle
        )
}