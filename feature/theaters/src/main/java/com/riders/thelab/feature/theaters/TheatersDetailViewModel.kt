package com.riders.thelab.feature.theaters

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
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

    private val _tmdbItemUiState: MutableStateFlow<TMDBItemModel> = MutableStateFlow(TMDBItemModel())
    val tmdbItemUiState: StateFlow<TMDBItemModel> = _tmdbItemUiState

    ////////////////////////////////////////
    // Composable methods
    ////////////////////////////////////////
    private fun updateTMDBItemUiState(newItem: TMDBItemModel) {
        this._tmdbItemUiState.value = newItem
        Timber.e("updateTMDBItemUiState() | _tmdbItemUiState: ${_tmdbItemUiState.value.toString()}")
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
            bundle.getString(TheatersDetailActivity.EXTRA_TMDB_ITEM)?.let { extraMovieJsonString ->
                Json
                    .decodeFromString<TMDBItemModel>(extraMovieJsonString)
                    .runCatching {
                        updateTMDBItemUiState(this)
                    }
                    .onFailure {
                        Timber.e("runCatching - onFailure() | error caught: ${it.message}")
                    }
                    .onSuccess {
                        Timber.d("runCatching - onSuccess() |  Bundle is not null. movie: ${_tmdbItemUiState.value}")
                    }
            }
        }
    }
}