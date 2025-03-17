package com.riders.thelab.core.speechtotext

import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class VoiceManagedViewModel(
    private val voiceRecognizer: SpeechToTextRepository
) : BaseViewModel() {
    val commandState: Flow<VoiceCommandEntity> = voiceRecognizer.commandsFlow.map { it.first }

    init {
        viewModelScope.launch {
            commandState.collect {
                Timber.d("init | collect : $it")
            }
        }
    }

    override fun onCleared() {
        release()
        super.onCleared()
    }

    fun startRecognition() {
        voiceRecognizer.startRecognition()
    }

    fun release() {
        voiceRecognizer.release()
    }
}