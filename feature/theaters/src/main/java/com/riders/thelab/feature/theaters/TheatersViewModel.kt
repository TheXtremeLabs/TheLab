package com.riders.thelab.feature.theaters

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.common.network.NetworkState
import com.riders.thelab.core.common.storage.LabFileManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.bean.MovieCategoryEnum
import com.riders.thelab.core.data.local.bean.MovieEnum
import com.riders.thelab.core.data.local.model.Movie
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBMoviesUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTrendingMovieItemUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTrendingTvShowItemUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTvShowsUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBUpcomingMoviesUiState
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@Suppress("EmptyMethod")
@HiltViewModel
class TheatersViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel() {

    ////////////////////////////////////////
    // Compose states
    ////////////////////////////////////////
    val categories: List<String> = listOf("MOVIES", "TV SHOWS")

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    var mJosnAssetsMovies: MutableList<Movie> = mutableListOf()
    var mEnumMovies: MutableList<Movie> = mutableListOf()
    var mTrendingMovies: MutableList<Movie> = mutableListOf()
    var mUpcomingMovies: MutableList<Movie> = mutableListOf()
    var mPopularMovies: MutableList<Movie> = mutableListOf()

    ////////////////////////////////////////
    // Composable methods
    ////////////////////////////////////////
    private var _mTMDBTrendingMovieItemUiState: MutableStateFlow<TMDBTrendingMovieItemUiState> =
        MutableStateFlow(TMDBTrendingMovieItemUiState.Loading)
    var tmdbTrendingMovieItemUiState: StateFlow<TMDBTrendingMovieItemUiState> =
        _mTMDBTrendingMovieItemUiState

    private var _mTMDBMoviesUiState: MutableStateFlow<TMDBMoviesUiState> = MutableStateFlow(
        TMDBMoviesUiState.Loading
    )
    var tmdbMoviesUiState: StateFlow<TMDBMoviesUiState> = _mTMDBMoviesUiState

    private var _mTMDBUpcomingMoviesUiState: MutableStateFlow<TMDBUpcomingMoviesUiState> =
        MutableStateFlow(
            TMDBUpcomingMoviesUiState.Loading
        )
    var tmdbUpcomingMoviesUiState: StateFlow<TMDBUpcomingMoviesUiState> =
        _mTMDBUpcomingMoviesUiState

    private var _mTMDBTrendingTvShowItemUiState: MutableStateFlow<TMDBTrendingTvShowItemUiState> =
        MutableStateFlow(
            TMDBTrendingTvShowItemUiState.Loading
        )
    var tmdbTrendingTvShowItemUiState: StateFlow<TMDBTrendingTvShowItemUiState> =
        _mTMDBTrendingTvShowItemUiState

    private var _mTMDBTrendingTvShowsUiState: MutableStateFlow<TMDBTvShowsUiState> =
        MutableStateFlow(
            TMDBTvShowsUiState.Loading
        )
    var tmdbTrendingTvShowsUiState: StateFlow<TMDBTvShowsUiState> =
        _mTMDBTrendingTvShowsUiState

    var once by mutableStateOf(false)
        private set

    var tabRowSelected by mutableIntStateOf(0)
        private set

    var hasConnection by mutableStateOf(false)
        private set

    var isRefreshing by mutableStateOf(false)
        private set


    fun updateOnce() {
        once = true
    }

    fun updateTabRowSelected(selected: Int) {
        this.tabRowSelected = selected
    }

    private fun updateTMDBTrendingMovieItemUiState(newState: TMDBTrendingMovieItemUiState) {
        this._mTMDBTrendingMovieItemUiState.value = newState
    }

    private fun updateTMDBMoviesUiState(newState: TMDBMoviesUiState) {
        this._mTMDBMoviesUiState.value = newState
    }

    private fun updateTMDBUpcomingMoviesUiState(newState: TMDBUpcomingMoviesUiState) {
        this._mTMDBUpcomingMoviesUiState.value = newState
    }

    private fun updateTMDBTrendingTvShowItemUiState(newState: TMDBTrendingTvShowItemUiState) {
        this._mTMDBTrendingTvShowItemUiState.value = newState
    }

    private fun updateTMDBTvShowsUiState(newState: TMDBTvShowsUiState) {
        this._mTMDBTrendingTvShowsUiState.value = newState
    }

    private fun updateTrendingMovieList(trendingMovies: List<Movie>) {
        this.mTrendingMovies.addAll(trendingMovies)
    }

    private fun updateUpcomingMovieList(upcomingMovies: List<Movie>) {
        this.mUpcomingMovies.addAll(upcomingMovies)
    }

    private fun updatePopularMovieList(popularMovies: List<Movie>) {
        this.mPopularMovies.addAll(popularMovies)
    }

    private fun updateHasInternetConnection(hasConnection: Boolean) {
        this.hasConnection = hasConnection
    }

    fun updateIsRefreshing(isRefreshing: Boolean) {
        this.isRefreshing = isRefreshing
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }


    ////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////
    fun observeNetworkState(networkManager: LabNetworkManager) {
        Timber.d("observeNetworkState()")

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            networkManager.getNetworkState().collect { networkState ->
                when (networkState) {
                    is NetworkState.Available -> {
                        Timber.d("network state is Available. All set.")
                        updateHasInternetConnection(true)

                        fetchTMDBData()
                    }

                    is NetworkState.Losing -> {
                        Timber.w("network state is Losing. Internet connection about to be lost")
                        updateHasInternetConnection(false)
                    }

                    is NetworkState.Lost -> {
                        Timber.e("network state is Lost. Should not allow network calls initialization")
                        updateHasInternetConnection(false)
                    }

                    is NetworkState.Unavailable -> {
                        Timber.e("network state is Unavailable. Should not allow network calls initialization")
                        updateHasInternetConnection(false)
                    }

                    is NetworkState.Undefined -> {
                        Timber.i("network state is Undefined. Do nothing")
                        updateHasInternetConnection(false)
                    }
                }
            }
        }
    }

    fun fetchTMDBData() {
        Timber.d("fetchTMDBData()")

        // Trending Movie
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            runCatching {
                val response = repository.getTrendingMovies()
                withContext(Dispatchers.Main) {
                    updateTMDBTrendingMovieItemUiState(
                        TMDBTrendingMovieItemUiState.Success(response)
                    )
                }
            }.onFailure { exception ->
                Timber.e("error caught with message: ${exception.message}")
                exception.message?.let {
                    withContext(Dispatchers.Main) {
                        updateTMDBTrendingMovieItemUiState(
                            TMDBTrendingMovieItemUiState.Error(it)
                        )
                    }
                }
            }
        }

        // Popular Movies
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            runCatching {
                val response = repository.getPopularMovies()
                withContext(Dispatchers.Main) {
                    updateTMDBMoviesUiState(
                        TMDBMoviesUiState.Success(response)
                    )
                }
            }.onFailure { exception ->
                Timber.e("error caught with message: ${exception.message}")
                exception.message?.let {
                    withContext(Dispatchers.Main) {
                        updateTMDBMoviesUiState(
                            TMDBMoviesUiState.Error(it)
                        )
                    }
                }
            }
        }

        // Upcoming Movies
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            runCatching {
                val response = repository.getUpcomingMovies()
                withContext(Dispatchers.Main) {
                    updateTMDBUpcomingMoviesUiState(
                        TMDBUpcomingMoviesUiState.Success(response)
                    )
                }
            }.onFailure { exception ->
                Timber.e("error caught with message: ${exception.message}")
                exception.message?.let {
                    withContext(Dispatchers.Main) {
                        updateTMDBUpcomingMoviesUiState(
                            TMDBUpcomingMoviesUiState.Error(it)
                        )
                    }
                }
            }
        }

        // Trending Tv Show Item
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            runCatching {
                val response = repository.getTrendingTvShows()
                withContext(Dispatchers.Main) {
                    updateTMDBTrendingTvShowItemUiState(
                        TMDBTrendingTvShowItemUiState.Success(response)
                    )
                }
            }.onFailure { exception ->
                Timber.e("error caught with message: ${exception.message}")
                exception.message?.let {
                    withContext(Dispatchers.Main) {
                        updateTMDBTrendingTvShowItemUiState(
                            TMDBTrendingTvShowItemUiState.Error(it)
                        )
                    }
                }
            }
        }

        // Tv Shows
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            runCatching {
                val response = repository.getPopularTvShows()
                withContext(Dispatchers.Main) {
                    updateTMDBTvShowsUiState(
                        TMDBTvShowsUiState.Success(response)
                    )
                }
            }.onFailure { exception ->
                Timber.e("error caught with message: ${exception.message}")
                exception.message?.let {
                    withContext(Dispatchers.Main) {
                        updateTMDBTvShowsUiState(
                            TMDBTvShowsUiState.Error(it)
                        )
                    }
                }
            }
        }
    }

    fun fetchMovies(context: Context) {
        Timber.d("fetchMovies()")

        var assetsMovies: List<Movie>? = null

        LabFileManager
            .getJsonFileContentFromAssets(context, "theaters_movie_list.json")
            ?.runCatching {
                assetsMovies = json.decodeFromString<List<Movie>>(this)
            }
            ?.onFailure {
                it.printStackTrace()
                Timber.e("Failed to open asset file. Message : ${it.message}")
            }

        Timber.d("Assets movie count : ${assetsMovies?.size}")

        val movies = MovieEnum.getMovies().toMutableList().run {
            if (!assetsMovies.isNullOrEmpty()) {
                this.addAll(assetsMovies!!)
            }
            this.toList()
        }

        updateTrendingMovieList(
            movies.filter { it.category == MovieCategoryEnum.TRENDING.value }
        )
        updateUpcomingMovieList(
            movies.filter { it.category == MovieCategoryEnum.UPCOMING.value }
        )
        updatePopularMovieList(
            movies.filter { it.category == MovieCategoryEnum.POPULAR.value }
        )
    }


    fun getTMDBItemDetail(activity: Context, item: TMDBItemModel) {
        Timber.d("getTMDBItemDetail() | movie: $item")

        Intent(activity, TheatersDetailActivity::class.java)
            .apply {
                putExtra(TheatersDetailActivity.EXTRA_TMDB_ITEM, json.encodeToString(item))
            }
            .runCatching {
                activity.startActivity(this)
            }
            .onFailure {
                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching - onSuccess() | Activity launched successfully")
            }
    }
}