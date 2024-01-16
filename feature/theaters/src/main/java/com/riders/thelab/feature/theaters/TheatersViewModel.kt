package com.riders.thelab.feature.theaters

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.storage.LabFileManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.bean.MovieCategoryEnum
import com.riders.thelab.core.data.local.bean.MovieEnum
import com.riders.thelab.core.data.local.model.Movie
import com.riders.thelab.core.data.local.model.compose.TMDBUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBMoviesUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTrendingMovieItemUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTrendingTvShowItemUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTvShowsUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBUpcomingMoviesUiState
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

@HiltViewModel
class TheatersViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    ////////////////////////////////////////
    // Compose states
    ////////////////////////////////////////
    var once by mutableStateOf(false)
        private set

    var mJosnAssetsMovies: MutableList<Movie> = mutableListOf()
    var mEnumMovies: MutableList<Movie> = mutableListOf()
    var mTrendingMovies: MutableList<Movie> = mutableListOf()
    var mUpcomingMovies: MutableList<Movie> = mutableListOf()
    var mPopularMovies: MutableList<Movie> = mutableListOf()

    ////////////////////////////////////////
    // Composable methods
    ////////////////////////////////////////
    private var _mTMDBTrendingMovieItemUiState: MutableStateFlow<TMDBTrendingMovieItemUiState> =
        MutableStateFlow(
            TMDBTrendingMovieItemUiState.Loading
        )
    private var tmdbTrendingMovieItemUiState: StateFlow<TMDBTrendingMovieItemUiState> =
        _mTMDBTrendingMovieItemUiState

    private var _mTMDBMoviesUiState: MutableStateFlow<TMDBMoviesUiState> = MutableStateFlow(
        TMDBMoviesUiState.Loading
    )
    private var tmdbMoviesUiState: StateFlow<TMDBMoviesUiState> = _mTMDBMoviesUiState

    private var _mTMDBUpcomingMoviesUiState: MutableStateFlow<TMDBUiState.TMDBUpcomingMoviesUiState> =
        MutableStateFlow(
            TMDBUpcomingMoviesUiState.Loading
        )
    private var tmdbUpcomingMoviesUiState: StateFlow<TMDBUpcomingMoviesUiState> =
        _mTMDBUpcomingMoviesUiState

    private var _mTMDBTrendingTvShowItemUiState: MutableStateFlow<TMDBTrendingTvShowItemUiState> =
        MutableStateFlow(
            TMDBTrendingTvShowItemUiState.Loading
        )
    private var tmdbTrendingTvShowItemUiState: StateFlow<TMDBTrendingTvShowItemUiState> =
        _mTMDBTrendingTvShowItemUiState

    private var _mTMDBTrendingTvShowsUiState: MutableStateFlow<TMDBTvShowsUiState> =
        MutableStateFlow(
            TMDBTvShowsUiState.Loading
        )
    private var tmdbTrendingTvShowsUiState: StateFlow<TMDBTvShowsUiState> =
        _mTMDBTrendingTvShowsUiState


    fun updateOnce() {
        once = true
    }

    private fun updateTMDBTrendingMovieItemUiState(newState: TMDBTrendingMovieItemUiState) {
        this._mTMDBTrendingMovieItemUiState.value = newState
    }

    private fun updateTMDBMoviesUiState(newState: TMDBMoviesUiState) {
        this._mTMDBMoviesUiState.value = newState
    }

    private fun updateTMDBUpcomingMoviesUiState(newState: TMDBUiState.TMDBUpcomingMoviesUiState) {
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
                assetsMovies = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }.decodeFromString<List<Movie>>(this)
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

    fun getMovieDetail(activity: Context, movie: Movie) {
        Timber.d("getMovieDetail() | movie: $movie")

        Intent(activity, TheatersDetailActivity::class.java)
            .apply {
                putExtra(TheatersDetailActivity.EXTRA_MOVIE, Json.encodeToString(movie))
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