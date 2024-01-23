package com.riders.thelab.feature.theaters

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import com.riders.thelab.core.data.local.model.compose.TMDBUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
 fun ScreenTvShowsContent(
 trendingTvShowItem: TMDBUiState.TMDBTrendingTvShowItemUiState,
 trendingTvShows: TMDBUiState.TMDBTvShowsUiState
) {
 val lazyListState = rememberLazyListState()
 val lazyRowTrendingListState = rememberLazyListState()
 val lazyRowUpcomingListState = rememberLazyListState()
 val lazyRowPopularListState = rememberLazyListState()

}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewScreenTvShowsContent() {}