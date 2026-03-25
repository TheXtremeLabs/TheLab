package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
object LoginFieldsUIState {
    @Stable
    @Immutable
    sealed class Login {
        @Stable
        @Immutable
        data object Ok : Login()

        @Stable
        @Immutable
        data class HasError(val errorMessage: String) : Login()

        @Stable
        @Immutable
        data object Idle : Login()
    }

    @Stable
    @Immutable
    sealed class Password {
        @Stable
        @Immutable
        data object Ok : Password()

        @Stable
        @Immutable
        data class HasError(val errorMessage: String) : Password()

        @Stable
        @Immutable
        data object Idle : Password()
    }
}