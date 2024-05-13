package com.riders.thelab.feature.theaters.main

import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel

sealed interface UiEvent {
    data class OnPullToRefresh(val isRefreshing: Boolean) : UiEvent
    data class OnUpdateTabRowSelected(val tabSelected: Int) : UiEvent
    data object OnFetchTMDBData : UiEvent
    data class OnItemDetailClicked(val item: TMDBItemModel) : UiEvent
}