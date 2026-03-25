package com.riders.thelab.core.data.local.model.compose


import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.biometric.AuthContext
import com.riders.thelab.core.data.remote.dto.ApiResponse

@Stable
sealed class LoginUiState {

    @Stable
    @Immutable
    data class UserSuccess(val user: User) : LoginUiState()

    @Stable
    @Immutable
    data class UserError(
        val errorMessage: String? = null,
        val throwable: Throwable? = null
    ) : LoginUiState()

    @Stable
    @Immutable
    data class Success(val response: ApiResponse) : LoginUiState()

    @Stable
    @Immutable
    data class Error(val errorResponse: ApiResponse? = null) : LoginUiState()

    @Stable
    @Immutable
    data class Logged(
        val usernameField: String = "mijih",
        val passwordField: String = "kkhooh",

        /**
         * True when we want to render the "access with biometry" button
         */
        val canLoginWithBiometry: Boolean = false,

        /**
         * True when the user is logged in, false otherwise
         */
        val loggedIn: Boolean = false,

        /**
         * indicate that we should to show the biometric prompt to the user to enroll
         * the biometric token
         */
        val askBiometricEnrollment: Boolean = false,

        /**
         * Represent the Authentication context of our prompt
         */
        val authContext: AuthContext? = null
    ) : LoginUiState()

    @Stable
    @Immutable
    data object Connecting : LoginUiState()

    @Stable
    @Immutable
    data object Loading : LoginUiState()

    @Stable
    @Immutable
    data object None : LoginUiState()
}
