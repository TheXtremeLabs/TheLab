package com.riders.thelab.ui.login

sealed interface UiEvent {
    data class OnUpdateLogin(val newLogin: String) : UiEvent
    data class OnUpdatePassword(val newPassword: String) : UiEvent
    data class OnUpdateIsRememberCredentials(val checked:Boolean) : UiEvent
    data object OnLoginClicked : UiEvent
    data object OnSignUpClicked : UiEvent

    data object OnGoogleButtonLoginClicked : UiEvent
    data object OnLaunchMainActivity : UiEvent
}