package com.riders.thelab.core.ui.compose.component.dropdown

sealed interface UiEvent {
    data class OnUpdateQuery(val newValue: String) : UiEvent
    data object OnExpandedClicked : UiEvent
    data class OnExpandedChanged(val expanded: Boolean) : UiEvent
    data class OnOptionsSelected<T>(val data: T, val isUserAction: Boolean = false) : UiEvent
}