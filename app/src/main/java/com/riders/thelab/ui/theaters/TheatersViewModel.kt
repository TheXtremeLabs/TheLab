package com.riders.thelab.ui.theaters

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.riders.thelab.data.local.bean.MovieEnum
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TheatersViewModel @Inject constructor() : ViewModel() {

    var once by mutableStateOf(false)
        private set

    var movieList = mutableStateListOf<Movie>()

    fun updateOnce() {
        once = true
    }

    fun updateMovieList(movies: List<Movie>) {
        this.movieList.toMutableList().also { it.addAll(movies) }
    }


    ////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////
    fun fetchMovies(): Unit = updateMovieList(MovieEnum.getMovies())

    fun getMovieDetail(activity: Context, navigator: Navigator, movie: Movie) {
        Timber.d("getMovieDetail() | movie: ${movie.toString()}")

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