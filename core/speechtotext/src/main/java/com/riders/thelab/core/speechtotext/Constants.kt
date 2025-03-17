package com.riders.thelab.core.speechtotext

import android.media.AudioFormat

object Constants {
    const val HOSTNAME = "speech.googleapis.com"
    const val PORT = 443

    const val SAMPLE_RATE = 44000
    const val LANGUAGE_CODE = "en-US"
    const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
    const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_16BIT
    const val BUFFER_SIZE: Int = 6400
}