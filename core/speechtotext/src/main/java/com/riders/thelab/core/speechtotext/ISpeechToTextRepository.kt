package com.riders.thelab.core.speechtotext

import kotlinx.coroutines.flow.Flow

interface ISpeechToTextRepository {
    val commandsFlow: Flow<Pair<VoiceCommandEntity,String>>
    fun startRecognition()
    fun resume()
    fun pause()
    fun release()
}