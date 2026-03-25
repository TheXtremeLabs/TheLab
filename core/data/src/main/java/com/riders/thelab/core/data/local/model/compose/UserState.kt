package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * This class is for handling logging and database saving and errors state
 */
@Stable
sealed class UserState {
    @Stable
    @Immutable
    data object Saving : UserState()

    @Stable
    @Immutable
    data class Saved(val idSaved: Long) : UserState()

    @Stable
    @Immutable
    data class SavingError(val errorMessage: String) : UserState()

    @Stable
    @Immutable
    data object NotSaved : UserState()

    @Stable
    @Immutable
    data object Logged : UserState()

    @Stable
    @Immutable
    data object NotLogged : UserState()
}
