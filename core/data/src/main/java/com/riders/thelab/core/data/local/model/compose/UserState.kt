package com.riders.thelab.core.data.local.model.compose

/**
 * This class is for handling logging and database saving and errors state
 */
sealed class UserState {
    data object Saving : UserState()
    data class Saved(val idSaved: Long) : UserState()
    data class SavingError(val errorMessage: String) : UserState()
    data object NotSaved : UserState()
    data object Logged : UserState()
    data object NotLogged : UserState()
}
