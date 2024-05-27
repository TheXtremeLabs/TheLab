package com.riders.thelab.core.data.local.model.compose.theaters

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBTvShowsResponse

@Stable
sealed class TMDBUiState {
    // Movies UI states
    sealed class TMDBTrendingMovieItemUiState : TMDBUiState() {
        data class Success(val response: TMDBMovieResponse) : TMDBTrendingMovieItemUiState()
        data class Error(val message: String) : TMDBTrendingMovieItemUiState()
        data object Loading : TMDBTrendingMovieItemUiState()
    }


    sealed class TMDBMoviesUiState : TMDBUiState() {
        data class Success(val response: TMDBMovieResponse) : TMDBMoviesUiState()
        data class Error(val message: String) : TMDBMoviesUiState()
        data object Loading : TMDBMoviesUiState()
    }

    sealed class TMDBUpcomingMoviesUiState : TMDBUiState() {
        data class Success(val response: TMDBMovieResponse) : TMDBUpcomingMoviesUiState()
        data class Error(val message: String) : TMDBUpcomingMoviesUiState()
        data object Loading : TMDBUpcomingMoviesUiState()
    }


    // Tv Shows UI states
    sealed class TMDBTrendingTvShowItemUiState : TMDBUiState() {
        data class Success(val response: TMDBTvShowsResponse) : TMDBTrendingTvShowItemUiState()
        data class Error(val message: String) : TMDBTrendingTvShowItemUiState()
        data object Loading : TMDBTrendingTvShowItemUiState()
    }

    sealed class TMDBTvShowsUiState : TMDBUiState() {
        data class Success(val response: TMDBTvShowsResponse) : TMDBTvShowsUiState()
        data class Error(val message: String) : TMDBTvShowsUiState()
        data object Loading : TMDBTvShowsUiState()
    }

    sealed class TMDBDetailUiState : TMDBUiState() {
        data class Success(val item: TMDBItemModel) : TMDBDetailUiState()
        data class Error(val message: String) : TMDBDetailUiState()
        data object Loading : TMDBDetailUiState()
    }
}