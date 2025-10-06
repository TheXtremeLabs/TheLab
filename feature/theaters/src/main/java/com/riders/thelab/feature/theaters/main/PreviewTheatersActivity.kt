package com.riders.thelab.feature.theaters.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.theaters.TMDBUiState
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.ui.compose.NoInternetConnection
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV
import com.riders.thelab.feature.theaters.splashscreen.TheatersSplash
import com.riders.thelab.feature.theaters.splashscreen.TheatersSplashTV
import kotlinx.coroutines.delay


val trendingItemImageHeight: Dp = 600.dp


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TheatersContainer(
    theme: AppTheme,
    darkTheme: Boolean,
    hasNetworkConnection: Boolean,
    isActivitiesSplashScreenEnable: Boolean,
    categories: List<String>,
    tabRowSelected: Int,
    trendingMovieItem: TMDBUiState.TMDBTrendingMovieItemUiState,
    movies: TMDBUiState.TMDBMoviesUiState,
    upcomingMovies: TMDBUiState.TMDBUpcomingMoviesUiState,
    trendingTvShowItem: TMDBUiState.TMDBTrendingTvShowItemUiState,
    trendingTvShows: TMDBUiState.TMDBTvShowsUiState,
    isRefreshing: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val switch = remember { mutableStateOf(false) }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isActivitiesSplashScreenEnable) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.Center),
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -height } + fadeOut())
                    },
                    targetState = if (LocalInspectionMode.current) true else switch.value,
                    label = "splashscreen animation"
                ) { targetState ->
                    if (!targetState) {
                        TheatersSplash(theme = theme, darkTheme = darkTheme)
                    } else {
                        TheatersContent(
                            theme = theme,
                            darkTheme = darkTheme,
                            hasNetworkConnection = hasNetworkConnection,
                            categories = categories,
                            tabRowSelected = tabRowSelected,
                            trendingMovieItem = trendingMovieItem,
                            movies = movies,
                            upcomingMovies = upcomingMovies,
                            trendingTvShowItem = trendingTvShowItem,
                            trendingTvShows = trendingTvShows,
                            isRefreshing = isRefreshing,
                            uiEvent = uiEvent
                        )
                    }
                }
            } else {
                TheatersContent(
                    theme = theme, darkTheme = darkTheme,
                    hasNetworkConnection = hasNetworkConnection,
                    categories = categories,
                    tabRowSelected = tabRowSelected,
                    trendingMovieItem = trendingMovieItem,
                    movies = movies,
                    upcomingMovies = upcomingMovies,
                    trendingTvShowItem = trendingTvShowItem,
                    trendingTvShows = trendingTvShows,
                    isRefreshing = isRefreshing,
                    uiEvent = uiEvent
                )
            }
        }
    }

    LaunchedEffect(switch) {
        delay(3000L)
        switch.value = true
//        viewModel.updateOnce()
    }
}


@Composable
fun TheatersContainerTV(
    theme: AppTheme,
    darkTheme: Boolean,
    hasNetworkConnection: Boolean,
    tabRowSelected: Int,
    trendingMovieItem: TMDBUiState.TMDBTrendingMovieItemUiState,
    movies: TMDBUiState.TMDBMoviesUiState,
    upcomingMovies: TMDBUiState.TMDBUpcomingMoviesUiState,
    trendingTvShowItem: TMDBUiState.TMDBTrendingTvShowItemUiState,
    trendingTvShows: TMDBUiState.TMDBTvShowsUiState,
    isRefreshing: Boolean,
    uiEvent: (UiEvent) -> Unit
) {

    var switch by remember { mutableStateOf(false) }
    val categories = listOf("Movies", "Tv Shows")

    TheLabThemeTV(
        theme = theme,
        darkTheme = darkTheme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.tv.material3.MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                modifier = Modifier.matchParentSize(),
                targetState = hasNetworkConnection
            ) { targetState ->
                if (!targetState) {
                    NoInternetConnection(
                        modifier = Modifier.align(Alignment.Center),
                        theme = theme,
                        darkTheme = darkTheme,
                        action = {}
                    )
                } else {
                    AnimatedContent(
                        modifier = Modifier.align(Alignment.Center),
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()).togetherWith(
                                slideOutVertically { height -> -height } + fadeOut())
                        },
                        targetState = if (LocalInspectionMode.current) true else switch,
                        label = "splashscreen animation"
                    ) { targetState ->
                        if (!targetState) {
                            TheatersSplashTV(theme = theme, darkTheme = darkTheme)
                        } else {
                            TheatersContentTV(
                                theme = theme,
                                darkTheme = darkTheme,
                                hasNetworkConnection = hasNetworkConnection,
                                categories = categories,
                                tabRowSelected = tabRowSelected,
                                trendingMovieItem = trendingMovieItem,
                                movies = movies,
                                upcomingMovies = upcomingMovies,
                                trendingTvShowItem = trendingTvShowItem,
                                trendingTvShows = trendingTvShows,
                                isRefreshing = isRefreshing,
                                uiEvent = uiEvent
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(switch) {
        delay(3000L)
        switch = true
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewTheatersContainerWithoutConnection(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        TheatersContainer(
            theme = appTheme,
            darkTheme = true ,
            isActivitiesSplashScreenEnable = false,
            hasNetworkConnection = false,
            categories = listOf("Movies", "Tv Shows"),
            tabRowSelected = 0,
            trendingMovieItem = TMDBUiState.TMDBTrendingMovieItemUiState.Loading,
            movies = TMDBUiState.TMDBMoviesUiState.Success(TMDBMovieResponse.mockTMDBMovieResponse),
            upcomingMovies = TMDBUiState.TMDBUpcomingMoviesUiState.Loading,
            trendingTvShowItem = TMDBUiState.TMDBTrendingTvShowItemUiState.Loading,
            trendingTvShows = TMDBUiState.TMDBTvShowsUiState.Loading,
            isRefreshing = false,
            uiEvent = {}
        )
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewTheatersContainerWithConnection(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        TheatersContainer(
            theme = appTheme,
            darkTheme = true ,
            isActivitiesSplashScreenEnable = false,
            hasNetworkConnection = true,
            categories = listOf("Movies", "Tv Shows"),
            tabRowSelected = 0,
            trendingMovieItem = TMDBUiState.TMDBTrendingMovieItemUiState.Loading,
            movies = TMDBUiState.TMDBMoviesUiState.Success(TMDBMovieResponse.mockTMDBMovieResponse),
            upcomingMovies = TMDBUiState.TMDBUpcomingMoviesUiState.Loading,
            trendingTvShowItem = TMDBUiState.TMDBTrendingTvShowItemUiState.Loading,
            trendingTvShows = TMDBUiState.TMDBTvShowsUiState.Loading,
            isRefreshing = false,
            uiEvent = {}
        )
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewTheatersTV(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        TheatersContainerTV(
            theme = appTheme,
            darkTheme = true ,
            hasNetworkConnection = true,
            tabRowSelected = 0,
            trendingMovieItem = TMDBUiState.TMDBTrendingMovieItemUiState.Loading,
            movies = TMDBUiState.TMDBMoviesUiState.Success(TMDBMovieResponse.mockTMDBMovieResponse),
            upcomingMovies = TMDBUiState.TMDBUpcomingMoviesUiState.Loading,
            trendingTvShowItem = TMDBUiState.TMDBTrendingTvShowItemUiState.Loading,
            trendingTvShows = TMDBUiState.TMDBTvShowsUiState.Loading,
            isRefreshing = false,
            uiEvent = {}
        )
    }
}