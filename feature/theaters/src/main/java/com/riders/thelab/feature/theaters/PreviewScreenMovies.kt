package com.riders.thelab.feature.theaters

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
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
import com.riders.thelab.core.data.local.model.tmdb.toModel
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import timber.log.Timber


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
    onTMDBItemDetailClicked: (tmdbItemModel: TMDBItemModel) -> Unit
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = lazyListState
            ) {
                item {
                    AnimatedContent(targetState = trendingMovieItem, label = "") { targetState ->

                        if (targetState is TMDBTrendingMovieItemUiState.Success) {

                            val item: TMDBItemModel =
                                targetState.response.results.map { it.toModel() }[0]

                            TrendingTMDBItem(trendingItem = item) {
                                onTMDBItemDetailClicked(it)
                            }
                        }
                    }
                }

                // TRENDING
                item {
                    AnimatedContent(targetState = trendingMovieItem, label = "") { targetState ->
                        if (targetState is TMDBTrendingMovieItemUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toModel() }

                            TheaterTMDBList(
                                rowListState = lazyRowTrendingListState,
                                categoryTitle = MovieCategoryEnum.TRENDING.value,
                                tmdbList = tmdbList
                            ) { item: TMDBItemModel ->
                                Timber.d(item.toString())
                                onTMDBItemDetailClicked(item)
                            }
                        }
                    }
                }

                // UPCOMING
                item {
                    AnimatedContent(targetState = upcomingMovies, label = "") { targetState ->

                        if (targetState is TMDBUpcomingMoviesUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toModel() }

                            TheaterTMDBList(
                                rowListState = lazyRowUpcomingListState,
                                categoryTitle = MovieCategoryEnum.UPCOMING.value,
                                tmdbList = tmdbList
                            ) {
                                Timber.d(it.toString())
                                onTMDBItemDetailClicked(it)
                            }
                        }
                    }
                }

                // POPULAR
                item {
                    AnimatedContent(targetState = movies, label = "") { targetState ->

                        if (targetState is TMDBMoviesUiState.Success) {
                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toModel() }

                            TheaterTMDBList(
                                rowListState = lazyRowPopularListState,
                                categoryTitle = MovieCategoryEnum.POPULAR.value,
                                tmdbList = tmdbList
                            ) {
                                Timber.d(it.toString())
                                onTMDBItemDetailClicked(it)
                            }
                        }
                    }
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
            onTMDBItemDetailClicked = {}
        )
    }
}