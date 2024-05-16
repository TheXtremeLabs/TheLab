package com.riders.thelab.feature.googledrive.ui

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.riders.thelab.core.data.local.model.google.GoogleAccountModel

sealed interface UiEvent {
    data class OnHandleAccount(val account: GoogleAccountModel): UiEvent
    data class OnHandleTokenId(val newToken: String): UiEvent
    data object OnDisconnectRequest : UiEvent
    data class OnHandleSignInResult(val task: Task<GoogleSignInAccount>): UiEvent
}