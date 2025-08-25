package com.riders.thelab.feature.theaters.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.bean.MovieCategoryEnum
import com.riders.thelab.core.data.local.model.compose.theaters.TMDBUiState.TMDBMoviesUiState
import com.riders.thelab.core.data.local.model.compose.theaters.TMDBUiState.TMDBTrendingMovieItemUiState
import com.riders.thelab.core.data.local.model.compose.theaters.TMDBUiState.TMDBUpcomingMoviesUiState
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.local.model.tmdb.toItemModel
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun ScreenMovieContent(
    theme: AppTheme, darkTheme: Boolean,
    trendingMovieItem: TMDBTrendingMovieItemUiState,
    movies: TMDBMoviesUiState,
    upcomingMovies: TMDBUpcomingMoviesUiState,
    uiEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val lazyRowTrendingListState = rememberLazyListState()
    val lazyRowUpcomingListState = rememberLazyListState()
    val lazyRowPopularListState = rememberLazyListState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier.size(width = this.maxWidth, height = this.maxHeight),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = lazyListState
            ) {
                item {
                    AnimatedContent(targetState = trendingMovieItem, label = "") { targetState ->

                        if (targetState is TMDBTrendingMovieItemUiState.Success) {

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
                    AnimatedContent(targetState = trendingMovieItem, label = "") { targetState ->
                        if (targetState is TMDBTrendingMovieItemUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toItemModel() }

                            TheaterTMDBList(
                                theme = theme,
                                darkTheme = darkTheme,
                                rowListState = lazyRowTrendingListState,
                                categoryTitle = MovieCategoryEnum.TRENDING.value,
                                tmdbList = tmdbList,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                // UPCOMING
                item {
                    AnimatedContent(targetState = upcomingMovies, label = "") { targetState ->

                        if (targetState is TMDBUpcomingMoviesUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toItemModel() }

                            TheaterTMDBList(
                                theme = theme,
                                darkTheme = darkTheme,
                                rowListState = lazyRowUpcomingListState,
                                categoryTitle = MovieCategoryEnum.UPCOMING.value,
                                tmdbList = tmdbList,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                // POPULAR
                item {
                    AnimatedContent(targetState = movies, label = "") { targetState ->

                        if (targetState is TMDBMoviesUiState.Success) {
                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toItemModel() }

                            TheaterTMDBList(
                                theme = theme,
                                darkTheme = darkTheme,
                                rowListState = lazyRowPopularListState,
                                categoryTitle = MovieCategoryEnum.POPULAR.value,
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

@Composable
fun ScreenMovieContentTV(
    theme: AppTheme,
    darkTheme: Boolean,
    trendingMovieItem: TMDBTrendingMovieItemUiState,
    movies: TMDBMoviesUiState,
    upcomingMovies: TMDBUpcomingMoviesUiState,
    modifier : Modifier = Modifier,
    uiEvent: (UiEvent) -> Unit
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val lazyListState = rememberLazyListState()
    val lazyRowTrendingListState = rememberLazyListState()
    val lazyRowUpcomingListState = rememberLazyListState()
    val lazyRowPopularListState = rememberLazyListState()

    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .size(width = this.maxWidth, height = this.maxHeight),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = lazyListState
            ) {
                item {
                    AnimatedContent(targetState = trendingMovieItem, label = "") { targetState ->

                        if (targetState is TMDBTrendingMovieItemUiState.Success) {

                            val item: TMDBItemModel =
                                targetState.response.results.map { it.toItemModel() }[0]

                            TrendingTMDBItemTV(
                                theme = theme,
                                darkTheme = darkTheme,
                                modifier = Modifier
                                    .defaultMinSize(1.dp)
                                    .height(376.dp),
                                trendingItem = item,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                // TRENDING
                item {
                    AnimatedContent(targetState = trendingMovieItem, label = "") { targetState ->
                        if (targetState is TMDBTrendingMovieItemUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toItemModel() }

                            TheaterTMDBListTV(
                                theme = theme,
                                darkTheme = darkTheme,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(376.dp)
                                    .bringIntoViewRequester(bringIntoViewRequester)  ,
                                rowListState = lazyRowTrendingListState,
                                categoryTitle = MovieCategoryEnum.TRENDING.value,
                                tmdbList = tmdbList,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                // UPCOMING
                item {
                    AnimatedContent(targetState = upcomingMovies, label = "") { targetState ->

                        if (targetState is TMDBUpcomingMoviesUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toItemModel() }

                            TheaterTMDBListTV(
                                theme = theme,
                                darkTheme = darkTheme,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(376.dp)
                                    .bringIntoViewRequester(bringIntoViewRequester)  ,
                                rowListState = lazyRowUpcomingListState,
                                categoryTitle = MovieCategoryEnum.UPCOMING.value,
                                tmdbList = tmdbList,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }

                // POPULAR
                item {
                    AnimatedContent(targetState = movies, label = "") { targetState ->

                        if (targetState is TMDBMoviesUiState.Success) {
                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toItemModel() }

                            TheaterTMDBListTV(
                                theme = theme,
                                darkTheme = darkTheme,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(376.dp)
                                    .bringIntoViewRequester(bringIntoViewRequester)  ,
                                rowListState = lazyRowPopularListState,
                                categoryTitle = MovieCategoryEnum.POPULAR.value,
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
private fun PreviewScreenMoviesContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        ScreenMovieContent(
            theme = appTheme, darkTheme = isSystemInDarkTheme(),
            trendingMovieItem = TMDBTrendingMovieItemUiState.Success(
                TMDBMovieResponse.mockTMDBMovieResponse
            ),
            movies = TMDBMoviesUiState.Loading,
            upcomingMovies = TMDBUpcomingMoviesUiState.Error("Error message"),
        ) {}
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewScreenMoviesContentTV() {
    TheLabThemeTV(theme = AppTheme.Default) {
        ScreenMovieContentTV(
            theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(),
            trendingMovieItem = TMDBTrendingMovieItemUiState.Success(
                TMDBMovieResponse.mockTMDBMovieResponse
            ),
            movies = TMDBMoviesUiState.Loading,
            upcomingMovies = TMDBUpcomingMoviesUiState.Error("Error message"),
        ) {}
    }
}