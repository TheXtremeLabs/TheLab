package com.riders.thelab.core.data.local.model.compose


import com.riders.thelab.core.data.local.model.biometric.AuthContext
import com.riders.thelab.core.data.remote.dto.ApiResponse

sealed class LoginUiState {
    data class Success(val response: ApiResponse) : LoginUiState()
    data class Error(val errorResponse: ApiResponse? = null) : LoginUiState()

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

    object Connecting : LoginUiState()
    object Loading : LoginUiState()
    object None : LoginUiState()
}
