package com.riders.thelab.feature.googledrive.ui

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

sealed interface UiEvent {

    data class OnHandleSignInResult(val task: Task<GoogleSignInAccount>): UiEvent
}