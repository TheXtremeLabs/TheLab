package com.riders.thelab.feature.transitions.chooser

sealed interface UiEvent {
    data object OnTransitionsComposeClicked: UiEvent
    data object OnTransitionsXmlClicked: UiEvent
}