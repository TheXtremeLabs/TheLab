package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable

@Stable
sealed class TMDBViewState {
    data object Movies : TMDBViewState()
    data object TvShows : TMDBViewState()
}