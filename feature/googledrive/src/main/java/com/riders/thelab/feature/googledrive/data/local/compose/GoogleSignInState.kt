package com.riders.thelab.feature.googledrive.data.local.compose

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.google.GoogleAccountModel

@Stable
sealed interface GoogleSignInState {
    data class Connected(val account: GoogleAccountModel) : GoogleSignInState
    data object Disconnected : GoogleSignInState
    data object Loading : GoogleSignInState
}