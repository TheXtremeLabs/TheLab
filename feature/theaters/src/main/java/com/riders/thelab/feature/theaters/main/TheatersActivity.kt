package com.riders.thelab.feature.theaters.main

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.theaters.detail.TheatersDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber

@AndroidEntryPoint
class TheatersActivity : BaseComponentActivity(), KeyEvent.Callback {

    private val mTheatersViewModel: TheatersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {

                setContent {
                    val hasNetworkConnection by mTheatersViewModel.hasInternetConnection.collectAsStateWithLifecycle()

                    val theme: AppTheme by mTheatersViewModel.uiRepository
                        .getTheme()
                        .collectAsStateWithLifecycle(initialValue = AppTheme.Default)

                    val trendingMovieItem by mTheatersViewModel.tmdbTrendingMovieItemUiState.collectAsStateWithLifecycle()
                    val movies by mTheatersViewModel.tmdbMoviesUiState.collectAsStateWithLifecycle()
                    val upcomingMovies by mTheatersViewModel.tmdbUpcomingMoviesUiState.collectAsStateWithLifecycle()
                    val trendingTvShowItem by mTheatersViewModel.tmdbTrendingTvShowItemUiState.collectAsStateWithLifecycle()
                    val trendingTvShows by mTheatersViewModel.tmdbTrendingTvShowsUiState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = true) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                    if(isTv) {
                        TheatersContainerTV(
                            theme = theme,
                            darkTheme = true,
                            hasNetworkConnection = hasNetworkConnection,
                            tabRowSelected = mTheatersViewModel.tabRowSelected,
                            trendingMovieItem = trendingMovieItem,
                            movies = movies,
                            upcomingMovies = upcomingMovies,
                            trendingTvShowItem = trendingTvShowItem,
                            trendingTvShows = trendingTvShows,
                            isRefreshing = mTheatersViewModel.isRefreshing,
                            uiEvent = { event ->
                                when (event) {
                                    is UiEvent.OnItemDetailClicked -> launchTMDBItemDetailActivity(
                                        event.item
                                    )

                                    else -> mTheatersViewModel.onEvent(event)
                                }
                            }
                        )
                    } else {
                            TheatersContainer(
                                theme = theme,
                                darkTheme = true,
                                hasNetworkConnection = hasNetworkConnection,
                                isActivitiesSplashScreenEnable = mTheatersViewModel.isActivitiesSplashEnabled,
                                categories = mTheatersViewModel.categories,
                                tabRowSelected = mTheatersViewModel.tabRowSelected,
                                trendingMovieItem = trendingMovieItem,
                                movies = movies,
                                upcomingMovies = upcomingMovies,
                                trendingTvShowItem = trendingTvShowItem,
                                trendingTvShows = trendingTvShows,
                                isRefreshing = mTheatersViewModel.isRefreshing,
                                uiEvent = { event ->
                                    when (event) {
                                        is UiEvent.OnItemDetailClicked -> launchTMDBItemDetailActivity(
                                            event.item
                                        )

                                        else -> mTheatersViewModel.onEvent(event)
                                    }
                                }
                            )
                        }
                    }

                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mTheatersViewModel.fetchTMDBData()
    }

    override fun backPressed() {
        finish()
    }


    private fun launchTMDBItemDetailActivity(item: TMDBItemModel) {
        Timber.d("launchTMDBItemDetailActivity() | movie: $item")

        Intent(this@TheatersActivity, TheatersDetailActivity::class.java)
            .apply {
                putExtra(TheatersDetailActivity.EXTRA_TMDB_ITEM, Json.encodeToString(item))
            }
            .runCatching {
                startActivity(this)
            }
            .onFailure {
                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching - onSuccess() | Activity launched successfully")
            }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.d("onKeyUp() | event: ${event?.keyCode?.toString()}")
        when(event?.keyCode){
            KeyEvent.KEYCODE_DPAD_UP -> {
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {true}
            KeyEvent.KEYCODE_DPAD_LEFT-> {true}
            KeyEvent.KEYCODE_DPAD_RIGHT -> {true}
            KeyEvent.KEYCODE_DPAD_CENTER -> {true}
            KeyEvent.KEYCODE_BUTTON_SELECT -> {true}
            KeyEvent.KEYCODE_BACK -> {}
            else -> return true
        }

        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.d("onKeyDown() | event: ${event?.keyCode?.toString()}")
        return super.onKeyDown(keyCode, event)
    }
}