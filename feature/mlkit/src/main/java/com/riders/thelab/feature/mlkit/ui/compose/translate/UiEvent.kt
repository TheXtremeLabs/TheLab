package com.riders.thelab.feature.mlkit.ui.compose.translate

sealed interface UiEvent {
    data class OnUpdateInput(val newInput: String) : UiEvent
    data class OnSourceLanguageChanged(val newSource: String) : UiEvent
    data class OnTargetLanguageChanged(val newTarget: String) : UiEvent
    data object OnSwitchLanguages : UiEvent
    data object OnTranslate : UiEvent
}
