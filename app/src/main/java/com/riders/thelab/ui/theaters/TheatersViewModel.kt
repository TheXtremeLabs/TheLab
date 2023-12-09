package com.riders.thelab.ui.theaters

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.common.storage.LabFileManager
import com.riders.thelab.core.data.local.bean.MovieCategoryEnum
import com.riders.thelab.core.data.local.bean.MovieEnum
import com.riders.thelab.core.data.local.model.Movie
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TheatersViewModel @Inject constructor() : ViewModel() {

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
    fun updateOnce() {
        once = true
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


    ////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////
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

    fun getMovieDetail(activity: Context, navigator: Navigator, movie: Movie) {
        Timber.d("getMovieDetail() | movie: $movie")

        Intent(activity, TheatersDetailActivity::class.java)
            .apply {
                putExtra(TheatersDetailActivity.EXTRA_MOVIE, Json.encodeToString(movie))
            }
            .runCatching {
                navigator.callMultipaneDetailActivity(this)
            }
            .onFailure {
                Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching - onSuccess() | Activity launched successfully")
            }
    }
}