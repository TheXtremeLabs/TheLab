package com.riders.thelab.feature.theaters.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.bean.MovieCategoryEnum
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBMoviesUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTrendingMovieItemUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBUpcomingMoviesUiState
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.local.model.tmdb.toItemModel
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun ScreenMovieContent(
    trendingMovieItem: TMDBTrendingMovieItemUiState,
    movies: TMDBMoviesUiState,
    upcomingMovies: TMDBUpcomingMoviesUiState,
    uiEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val lazyRowTrendingListState = rememberLazyListState()
    val lazyRowUpcomingListState = rememberLazyListState()
    val lazyRowPopularListState = rememberLazyListState()

    TheLabTheme {

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

                            TrendingTMDBItem(trendingItem = item, uiEvent = uiEvent)
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
private fun PreviewScreenMoviesContent() {
    TheLabTheme {
        ScreenMovieContent(
            trendingMovieItem = TMDBTrendingMovieItemUiState.Success(
                TMDBMovieResponse.mockTMDBMovieResponse
            ),
            movies = TMDBMoviesUiState.Loading,
            upcomingMovies = TMDBUpcomingMoviesUiState.Error("Error message"),
        ) {}
    }
}