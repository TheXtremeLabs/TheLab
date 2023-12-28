package com.riders.thelab.core.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RawRes
import timber.log.Timber
import kotlin.math.log10
import kotlin.math.max

object LabAudioManager {
    /////////////////////////////////////////
    // Audio
    /////////////////////////////////////////
    private const val AUDIO_SAMPLE_RATE: Int = 16000 // 44100 for music
    private const val AUDIO_CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
    private const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_16BIT
    private const val AGC_OFF: Int = MediaRecorder.AudioSource.VOICE_RECOGNITION

    private val audioEncoding =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) AudioFormat.ENCODING_PCM_32BIT else AudioFormat.ENCODING_PCM_16BIT

    private var minBytes: Int = 0

    // Set sample rate to 44100hz
    private const val SAMPLE_RATE: Int = 44100
    private const val FFT_BINS: Int = 2048
    private const val UPDATE_MS: Int = 100

    private const val MEAN_MAX = 16384f // Maximum signal value


    @SuppressLint("MissingPermission")
    fun initRecordAudio(context: Context): AudioRecord? {
        Timber.d("initRecordAudio()")

        return if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Timber.e("Returns null because app doesn't have Audio permission.")
            null
        } else {

            minBytes = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                audioEncoding
            )

            minBytes = max(minBytes.toDouble(), FFT_BINS.toDouble()).toInt()

            // VOICE_RECOGNITION: use the mic with AGC turned off!
            val record = AudioRecord(
                AGC_OFF,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                audioEncoding,
                minBytes
            )

            Timber.d("Buffer size: $minBytes (${record.sampleRate} = $SAMPLE_RATE)")

            record
        }
    }

    fun getMinBytes() = minBytes

    /**
     * Convert the fft output to DB
     */
    fun convertToDb(data: DoubleArray, maxSquared: Double): DoubleArray {
        data[0] = db2(data[0], 0.0, maxSquared)
        var j = 1
        var i = 1
        while (i < data.size - 1) {
            data[j] = db2(data[i], data[i + 1], maxSquared)
            i += 2
            j++
        }
        data[j] = data[0]
        return data
    }

    /**
     * Compute db of bin, where "max" is the reference db
     * @param r Real part
     * @param i complex part
     */
    private fun db2(r: Double, i: Double, maxSquared: Double): Double {
        return 5.0 * log10((r * r + i * i) / maxSquared)
    }

    private var mediaPlayer: MediaPlayer? = null

    /**
     *
     */
    fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    fun play(context: Context, @RawRes resId: Int, completedCallback: () -> Unit) {
        stop()

        MediaPlayer.create(context, resId)
            .apply {
                mediaPlayer = this

                this.setOnCompletionListener {
                    stop()
                    completedCallback()
                }

                this.start()
            }
    }

    fun playSessionId(context: Context, audioRecordSessionId: Int, completedCallback: () -> Unit) {
        stop()

        MediaPlayer.create(context, audioRecordSessionId)
            .apply {
                mediaPlayer = this

                this.setOnCompletionListener {
                    stop()
                    completedCallback()
                }

                this.start()
            }
    }

    /**
     *
     */
    fun getAudioSessionId(): Int? = mediaPlayer?.audioSessionId
}