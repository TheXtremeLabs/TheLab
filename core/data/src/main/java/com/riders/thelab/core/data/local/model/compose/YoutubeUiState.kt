package com.riders.thelab.core.data.local.model.compose

import com.riders.thelab.core.data.local.model.youtube.Video
import kotools.types.text.NotBlankString

sealed interface YoutubeUiState {
    data class Success(val videos: List<Video>) : YoutubeUiState
    data class Error(val message: NotBlankString, val throwable: Throwable? = null) : YoutubeUiState
    data object Loading : YoutubeUiState
}