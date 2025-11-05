package com.riders.thelab.central.ui

import com.riders.thelab.core.data.local.model.app.PackageApp

sealed interface UiEvent {
    data object OnInfoClicked : UiEvent
    data object OnDismissBottomSheet : UiEvent

    data class OnPackageClicked(val packageItem: PackageApp) : UiEvent
}