package com.riders.thelab.feature.theaters.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.bean.MovieCategoryEnum
import com.riders.thelab.core.data.local.model.compose.theaters.TMDBUiState.TMDBTrendingTvShowItemUiState
import com.riders.thelab.core.data.local.model.compose.theaters.TMDBUiState.TMDBTvShowsUiState
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.local.model.tmdb.toItemModel
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBTvShowsResponse
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ScreenTvShowsContent(
    theme: AppTheme,
    darkTheme: Boolean,
    trendingTvShowItem: TMDBTrendingTvShowItemUiState,
    trendingTvShows: TMDBTvShowsUiState,
    uiEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val lazyRowTrendingListState = rememberLazyListState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = lazyListState
            ) {
                item {
                    AnimatedContent(targetState = trendingTvShowItem, label = "") { targetState ->

                        if (targetState is TMDBTrendingTvShowItemUiState.Success) {
                            val item: TMDBItemModel =
                                targetState.response.results.map { it.toItemModel() }[0]

                            TrendingTMDBItem(
                                theme = theme,
                                darkTheme = darkTheme,
                                trendingItem = item,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                // TRENDING
                item {
                    AnimatedContent(targetState = trendingTvShows, label = "") { targetState ->
                        if (targetState is TMDBTvShowsUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toItemModel() }

                            TheaterTMDBList(
                                theme = theme, darkTheme = darkTheme,
                                rowListState = lazyRowTrendingListState,
                                categoryTitle = MovieCategoryEnum.TRENDING.value,
                                tmdbList = tmdbList,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                item {
                    ProvidedBy(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        providerIcon = R.drawable.tmdb_logo,
                        hasPadding = false,
                        hasRoundedCorners = true
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ScreenTvShowsContentTV(
    theme: AppTheme,
    darkTheme: Boolean,
    trendingTvShowItem: TMDBTrendingTvShowItemUiState,
    trendingTvShows: TMDBTvShowsUiState,
    uiEvent: (UiEvent) -> Unit
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val lazyListState = rememberLazyListState()
    val lazyRowTrendingListState = rememberLazyListState()

    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = lazyListState
            ) {
                item {
                    AnimatedContent(targetState = trendingTvShowItem, label = "") { targetState ->

                        if (targetState is TMDBTrendingTvShowItemUiState.Success) {
                            val item: TMDBItemModel =
                                targetState.response.results.map { it.toItemModel() }[0]

                            TrendingTMDBItemTV(
                                theme = theme,
                                darkTheme = darkTheme,
                                trendingItem = item,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                // TRENDING
                item {
                    AnimatedContent(targetState = trendingTvShows, label = "") { targetState ->
                        if (targetState is TMDBTvShowsUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toItemModel() }

                            TheaterTMDBListTV(
                                theme = theme, darkTheme = darkTheme,
                                rowListState = lazyRowTrendingListState,
                                categoryTitle = MovieCategoryEnum.TRENDING.value,
                                tmdbList = tmdbList,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                item {
                    ProvidedBy(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        providerIcon = R.drawable.tmdb_logo,
                        hasPadding = false,
                        hasRoundedCorners = true
                    )
                }
            }
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewScreenTvShowsContent() {
    TheLabThemeTV(theme = AppTheme.Default) {
        ScreenTvShowsContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            trendingTvShowItem = TMDBTrendingTvShowItemUiState.Success(response = TMDBTvShowsResponse.mockTMDBTvShowsResponse),
            trendingTvShows = TMDBTvShowsUiState.Loading,
            uiEvent = {},
        )
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewScreenTvShowsContentTV() {

    TheLabThemeTV(theme = AppTheme.Default) {
        ScreenTvShowsContentTV(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            trendingTvShowItem = TMDBTrendingTvShowItemUiState.Success(response = TMDBTvShowsResponse.mockTMDBTvShowsResponse),
            trendingTvShows = TMDBTvShowsUiState.Loading,
            uiEvent = {},
        )
    }
}