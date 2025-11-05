package com.riders.thelab.central.ui

import com.riders.thelab.core.data.local.model.app.PackageApp

sealed interface UiEvent {

    data class OnUpdateSearchMode(val enabled: Boolean) : UiEvent
    data class OnUpdateSearchQuery(val newQuery: String) : UiEvent
    data object OnClearSearchQuery : UiEvent

    data class OnPackageClicked(val packageItem: PackageApp) : UiEvent
}