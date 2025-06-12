package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import android.graphics.Bitmap
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.music.MusicRecognitionModel

sealed interface UiEvent {

    data class UpdateCurrentPageIndex(val newIndex:Int): UiEvent
    data class UpdateMusicModelImageBase64(val currentSong:MusicRecognitionModel, val imageBitmap:Bitmap): UiEvent

    data class OpenInSpotify(val song: Song) : UiEvent
    data class OpenModelInSpotify(val model: MusicRecognitionModel) : UiEvent
}