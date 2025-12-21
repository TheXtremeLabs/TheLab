package com.riders.thelab.core.speechtotext

import android.annotation.SuppressLint
import android.media.AudioRecord
import android.media.MediaRecorder
import com.google.api.gax.rpc.ClientStream
import com.google.api.gax.rpc.ResponseObserver
import com.google.api.gax.rpc.StreamController
import com.google.cloud.speech.v1.RecognitionConfig
import com.google.cloud.speech.v1.SpeechClient
import com.google.cloud.speech.v1.SpeechContext
import com.google.cloud.speech.v1.StreamingRecognitionConfig
import com.google.cloud.speech.v1.StreamingRecognizeRequest
import com.google.cloud.speech.v1.StreamingRecognizeResponse
import com.google.protobuf.ByteString
import com.riders.thelab.core.common.utils.ioCoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SpeechToTextRepository @Inject constructor(
    private val _speechClient: SpeechClient
) : ISpeechToTextRepository {

    private var _clientStream: ClientStream<StreamingRecognizeRequest>? = null
    private var _voiceRecorder: AudioRecord? = null
    private var _isStreaming = true

    override val commandsFlow: Flow<Pair<VoiceCommandEntity, String>> = callbackFlow {
        val callback = object : ResponseObserver<StreamingRecognizeResponse> {
            override fun onStart(controller: StreamController?) {
                Timber.d("ResponseObserver.onStart")
            }

            override fun onResponse(response: StreamingRecognizeResponse?) {
                Timber.d("ResponseObserver.onResponse($response)")
                response?.let {
                    var text: String? = null
                    var isFinal = false
                    if (response.resultsCount > 0) {
                        val result = response.getResults(0)
                        isFinal = result.isFinal
                        if (result.alternativesCount > 0) {
                            val alternative = result.getAlternatives(0)
                            text = alternative.transcript
                        }
                    }

                    if (isFinal) text?.let {
                        Timber.d("text -[${response.getResults(0)}]")
                        Timber.d("final text: $it")
                        trySend(VoiceCommandEntity.processCommand(text) to text)
                    }
                }
            }

            override fun onError(t: Throwable?) {
                Timber.e("onError[${t?.message}]")
            }

            override fun onComplete() {
                Timber.d("onComplete $_clientStream $_voiceRecorder")
            }
        }

        _clientStream = _speechClient.streamingRecognizeCallable()?.splitCall(callback)
        _clientStream?.send(startRequest)

        awaitClose {
            _voiceRecorder?.release()
            _clientStream?.closeSend()
            _voiceRecorder = null
            _clientStream = null
        }
    }.shareIn(ioCoroutineScope, SharingStarted.Lazily, replay = 0)


    override fun startRecognition() {
        startRecording()
    }

    private fun startRecording() {
        if (_voiceRecorder != null) return
        ioCoroutineScope.launch {
            var minBufferSize = Constants.BUFFER_SIZE
            try {
                val buffer = ByteArray(minBufferSize)
                initVoiceRecorder()
                _voiceRecorder?.apply {
                    startRecording()
                    while (true) {
                        minBufferSize = read(buffer, 0, buffer.size)
                        recognize(buffer, minBufferSize)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun recognize(data: ByteArray?, size: Int) {
        if (!_isStreaming) return
        _clientStream?.send(
            StreamingRecognizeRequest.newBuilder()
                .setAudioContent(ByteString.copyFrom(data, 0, size))
                .build()
        )
    }

    override fun resume() {
        _isStreaming = true
    }

    override fun pause() {
        _isStreaming = false
    }

    private val sampleRate: Int
        get() = _voiceRecorder?.sampleRate ?: 0

    private val startRequest = StreamingRecognizeRequest.newBuilder()
        .setStreamingConfig(
            StreamingRecognitionConfig.newBuilder()
                .setConfig(
                    RecognitionConfig.newBuilder()
                        .addSpeechContexts(
                            SpeechContext.newBuilder()
                                .addAllPhrases(VoiceCommandEntity.getAllCommands())
                        )
                        .setLanguageCode(Constants.LANGUAGE_CODE)
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setSampleRateHertz(Constants.SAMPLE_RATE)
                        .build()
                )
                .setInterimResults(true)
                .setSingleUtterance(false)
                .build()
        ).build()

    @SuppressLint("MissingPermission")
    private fun initVoiceRecorder() = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        Constants.CHANNEL_CONFIG,
        Constants.AUDIO_FORMAT,
        Constants.BUFFER_SIZE * 10
    ).also {
        _voiceRecorder = it
    }

    override fun release() {
        _voiceRecorder?.release()
        _voiceRecorder = null
        _clientStream?.closeSend()
        _clientStream = null
    }
}