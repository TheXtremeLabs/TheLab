package com.riders.thelab.core.data.local.model.compose

object LoginFieldsUIState {
    sealed class Login {
        data object Ok : Login()
        data class HasError(val errorMessage: String) : Login()
        data object Idle : Login()
    }

    sealed class Password {
        data object Ok : Password()
        data class HasError(val errorMessage: String) : Password()
        data object Idle : Password()
    }
}