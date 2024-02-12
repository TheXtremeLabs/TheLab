package com.riders.thelab.feature.theaters

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.common.network.NetworkState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabHorizontalViewPagerGeneric
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.network.NoNetworkConnection
import com.riders.thelab.core.ui.compose.component.network.PreviewProviderNetworkState
import com.riders.thelab.core.ui.compose.component.tab.LabTabRow
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.launch
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheatersContent(
    viewModel: TheatersViewModel,
    networkState: NetworkState,
    isRefreshing: Boolean,
    onPullToRefresh: (Boolean) -> Unit
) {

    val activity: TheatersActivity = LocalContext.current as TheatersActivity
    val scope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { onPullToRefresh(true) }
    )

    val pagerState: PagerState = rememberPagerState { viewModel.categories.size }

    val trendingMovieItem by viewModel.tmdbTrendingMovieItemUiState.collectAsStateWithLifecycle()
    val movies by viewModel.tmdbMoviesUiState.collectAsStateWithLifecycle()
    val upcomingMovies by viewModel.tmdbUpcomingMoviesUiState.collectAsStateWithLifecycle()
    val trendingTvShowItem by viewModel.tmdbTrendingTvShowItemUiState.collectAsStateWithLifecycle()
    val trendingTvShows by viewModel.tmdbTrendingTvShowsUiState.collectAsStateWithLifecycle()

    val transitionSpec: () -> ContentTransform = {
        fadeIn(tween(durationMillis = 200)) togetherWith fadeOut(
            tween(
                durationMillis = 200
            )
        )
    }

    TheLabTheme(darkTheme = true) {
        Scaffold(
            topBar = {
                TheLabTopAppBar(
                    mainCustomContent = {
                        AnimatedContent(
                            targetState = networkState,
                            transitionSpec = { transitionSpec() },
                            label = "network state animation"
                        ) { targetState: NetworkState ->
                            when (targetState) {
                                is NetworkState.Available -> {
                                    LabTabRow(
                                        selectedItemIndex = viewModel.tabRowSelected,
                                        items = viewModel.categories
                                    ) {
                                        viewModel.updateTabRowSelected(it)
                                    }
                                }

                                else -> {
                                    Box {}
                                }
                            }
                        }
                    },
                    withGradientBackground = true,
                    navigationIconColor = Color.White
                ) {}
            }
        ) {

            AnimatedContent(
                targetState = networkState,
                transitionSpec = { transitionSpec() },
                label = "network state animation"
            ) { targetState: NetworkState ->
                when (targetState) {
                    is NetworkState.Available -> {

                        Box(
                            modifier = Modifier.pullRefresh(
                                state = pullRefreshState,
                                enabled = true
                            )
                        ) {
                            LabHorizontalViewPagerGeneric(
                                viewModel = viewModel,
                                pagerState = pagerState,
                                items = viewModel.categories,
                                autoScroll = false,
                                userScrollEnabled = false
                            ) { page, _ ->

                                when (page) {
                                    0 -> {
                                        ScreenMovieContent(
                                            trendingMovieItem,
                                            movies,
                                            upcomingMovies,
                                            onTMDBItemDetailClicked = {
                                                viewModel.getTMDBItemDetail(
                                                    activity,
                                                    it
                                                )
                                            }
                                        )
                                    }

                                    1 -> {
                                        ScreenTvShowsContent(
                                            trendingTvShowItem,
                                            trendingTvShows,
                                            onTMDBItemDetailClicked = {
                                                viewModel.getTMDBItemDetail(
                                                    activity,
                                                    it
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                        PullRefreshIndicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            refreshing = isRefreshing,
                            state = pullRefreshState
                        )
                    }
                }

                else -> {
                NoNetworkConnection()
            }
            }

            }
        }
    }

    LaunchedEffect(key1 = viewModel.tabRowSelected) {
        Timber.d("LaunchedEffect | tabRowSelected : ${viewModel.tabRowSelected} | with: ${this.coroutineContext}")

        scope.launch {
            pagerState.animateScrollToPage(viewModel.tabRowSelected)
        }
    }

    LaunchedEffect(key1 = isRefreshing) {
        Timber.d("LaunchedEffect | isRefreshing : $isRefreshing | with: ${this.coroutineContext}")

        if (isRefreshing) {
            onPullToRefresh(isRefreshing)
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
private fun PreviewTheatersContent(@PreviewParameter(PreviewProviderNetworkState::class) networkState: NetworkState) {
    val viewModel: TheatersViewModel = hiltViewModel()

    TheLabTheme(darkTheme = true) {
        TheatersContent(
            viewModel,
            NetworkState.Unavailable,
            isRefreshing = false,
            onPullToRefresh = { }
        )
    }
}