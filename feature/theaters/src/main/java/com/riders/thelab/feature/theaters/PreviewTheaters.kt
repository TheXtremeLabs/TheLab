package com.riders.thelab.feature.theaters

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabHorizontalViewPagerGeneric
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.tab.LabTabRow
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

val trendingItemImageHeight: Dp = 550.dp


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheatersContent(viewModel: TheatersViewModel) {

    val activity: TheatersActivity = LocalContext.current as TheatersActivity
    val scope = rememberCoroutineScope()

    val pagerState: PagerState = rememberPagerState { viewModel.categories.size }

    val trendingMovieItem by viewModel.tmdbTrendingMovieItemUiState.collectAsStateWithLifecycle()
    val movies by viewModel.tmdbMoviesUiState.collectAsStateWithLifecycle()
    val upcomingMovies by viewModel.tmdbUpcomingMoviesUiState.collectAsStateWithLifecycle()
    val trendingTvShowItem by viewModel.tmdbTrendingTvShowItemUiState.collectAsStateWithLifecycle()
    val trendingTvShows by viewModel.tmdbTrendingTvShowsUiState.collectAsStateWithLifecycle()

    TheLabTheme(darkTheme = true) {
        Scaffold(
            topBar = {
                TheLabTopAppBar(
                    mainCustomContent = {
                        LabTabRow(
                            selectedItemIndex = viewModel.tabRowSelected,
                            items = viewModel.categories
                        ) {
                            viewModel.updateTabRowSelected(it)
                        }
                    },
                    withGradientBackground = true,
                    navigationIconColor = Color.White
                ) {}
            }
        ) {
            LabHorizontalViewPagerGeneric(
                viewModel = viewModel,
                pagerState = pagerState,
                items = viewModel.categories,
                autoScroll = false,
                userScrollEnabled = false
            ) { page, _ ->

                // viewModel.updateTabRowSelected(page)

                when (page) {
                    0 -> {
                        ScreenMovieContent(
                            trendingMovieItem,
                            movies,
                            upcomingMovies,
                            onTMDBItemDetailClicked = { viewModel.getTMDBItemDetail(activity, it) }
                        )
                    }

                    1 -> {
                        ScreenTvShowsContent(
                            trendingTvShowItem,
                            trendingTvShows,
                            onTMDBItemDetailClicked = { viewModel.getTMDBItemDetail(activity, it) }
                        )
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
}

@Composable
fun TheatersContainer(viewModel: TheatersViewModel) {

    val switch = remember { mutableStateOf(false) }

    TheLabTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(md_theme_dark_background)
        ) {
            if (!viewModel.once) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.Center),
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -height } + fadeOut())
                    },
                    targetState = if (LocalInspectionMode.current) true else switch.value,
                    label = ""
                ) { targetState ->
                    if (!targetState) {
                        TheatersSplash()
                    } else {
                        TheatersContent(viewModel = viewModel)
                    }
                }
            } else {
                TheatersContent(viewModel = viewModel)
            }
        }
    }

    LaunchedEffect(switch) {
        delay(3000L)
        switch.value = true
        viewModel.updateOnce()
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////

@DevicePreviews
@Composable
private fun PreviewTheatersContent() {
    val viewModel: TheatersViewModel = hiltViewModel()
    TheLabTheme(darkTheme = true) {
        TheatersContent(viewModel)
    }
}

@DevicePreviews
@Composable
private fun PreviewTheatersContainer() {
    val viewModel: TheatersViewModel = hiltViewModel()
    TheLabTheme(darkTheme = true) {
        TheatersContainer(viewModel)
    }
}