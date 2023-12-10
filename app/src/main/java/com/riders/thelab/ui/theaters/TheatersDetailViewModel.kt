package com.riders.thelab.ui.theaters

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.data.local.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import timber.log.Timber

class TheatersDetailViewModel : ViewModel() {

    ////////////////////////////////////////
    // Variables
    ////////////////////////////////////////

    ////////////////////////////////////////
    // Compose states
    ////////////////////////////////////////
    var title by mutableStateOf("")
        private set

    private val _movieUiState: MutableStateFlow<Movie> = MutableStateFlow(Movie())
    val movieUiState: StateFlow<Movie> = _movieUiState

    ////////////////////////////////////////
    // Composable methods
    ////////////////////////////////////////
    private fun updateMovieUiState(newMovie: Movie) {
        this._movieUiState.value = newMovie
    }

    fun updateTitle(newTitle: String) {
        this.title = newTitle
    }


    ////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////
    fun getBundle(activity: Activity, intent: Intent) {
        val mBundle = intent.extras
        if (null == mBundle) {
            Timber.e("Bundle is null exit activity.")
            activity.finish()
        }

        mBundle?.let { bundle ->
            bundle.getString(TheatersDetailActivity.EXTRA_MOVIE)?.let { extraMovieJsonString ->
                Json
                    .decodeFromString<Movie>(extraMovieJsonString)
                    .runCatching {
                        updateMovieUiState(this)
                    }
                    .onFailure {
                        Timber.e("runCatching - onFailure() | error caught: ${it.message}")
                    }
                    .onSuccess {
                        Timber.d("runCatching - onSuccess() |  Bundle is not null. movie: ${_movieUiState.value}")
                    }
            }
        }
    }
}