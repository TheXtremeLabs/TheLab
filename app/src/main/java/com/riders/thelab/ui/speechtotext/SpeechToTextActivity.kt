package com.riders.thelab.ui.speechtotext

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivitySpeechToTextBinding
import timber.log.Timber
import java.util.*

class SpeechToTextActivity : AppCompatActivity(), RecognitionListener {

    lateinit var viewBinding: ActivitySpeechToTextBinding

    // Speech
    var speech: SpeechRecognizer? = null
    var recognizerIntent: Intent? = null
    var message: String? = null
    var currentFloatDB = 0f

    // TAG & Context
    private var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpeechToTextBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        context = this

        // Check permission
        Dexter.withContext(context)
            .withPermission(Manifest.permission.RECORD_AUDIO)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Timber.d("permission granted")
                    // Init Speech To Text Variables
                    initSpeechToText()

                    viewBinding.speechButton.setOnClickListener {

                        Timber.e("onSpeechClicked()")

                        if (viewBinding.speechInputTextView.text.toString().isNotEmpty())
                            viewBinding.speechInputTextView.text = ""

                        if (!isRecordingViewVisible()) {
                            // Display Recording view
                            showRecordingView()
                            viewBinding.speechButton.setColorFilter(
                                ContextCompat.getColor(
                                    this@SpeechToTextActivity,
                                    R.color.teal_700
                                )
                            )
                            showEq()
                        }


                        Timber.i("start listening ")
                        speech!!.startListening(recognizerIntent)
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Timber.d("permission denied")
                    finish()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .check()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


    override fun onDestroy() {
        if (speech != null) speech!!.stopListening()
        super.onDestroy()
    }


    ///////////////////////////////////////////
    //
    //  Class methods
    //
    ///////////////////////////////////////////
    /**
     * Init speech to text variables in order to use it
     */
    private fun initSpeechToText() {
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        speech!!.setRecognitionListener(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.packageName)
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
    }


    fun isRecordingViewVisible(): Boolean {
        if (View.VISIBLE == viewBinding.recordingTextView.visibility) return true
        else if (View.INVISIBLE == viewBinding.recordingTextView.visibility) return false
        return false
    }


    fun showRecordingView() {
        viewBinding.recordingTextView.visibility = View.VISIBLE
    }


    fun hideRecordingView() {
        viewBinding.recordingTextView.visibility = View.INVISIBLE
    }


    fun showEq() {
        viewBinding.eqImageView.setVisibility(View.VISIBLE)
    }


    fun hideEq() {
        viewBinding.eqImageView.setVisibility(View.INVISIBLE)
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Timber.e("onReadyForSpeech()")
    }

    override fun onBeginningOfSpeech() {
        Timber.i("onBeginningOfSpeech()")
    }

    override fun onRmsChanged(rmsdB: Float) {
        //Check if eqView is visible
        if (viewBinding.eqImageView.getVisibility() == View.VISIBLE) {

            // Variables
            val min = -50
            val toZero = 0
            val max = 50
            val random = Random()

            // Compare current dB value with the next one in order to set random value (negative/positive)
            if (currentFloatDB > rmsdB) {
                // Random negative
                val negRandom = random.nextInt(toZero - min) + toZero
                viewBinding.eqImageView.layoutParams.height =
                    viewBinding.eqImageView.layoutParams.height - negRandom
            } else if (currentFloatDB < rmsdB) {
                // Random positive
                val posRandom = random.nextInt(max + toZero) + toZero
                viewBinding.eqImageView.layoutParams.height =
                    viewBinding.eqImageView.layoutParams.height + posRandom
            }

            // eqImageView.getLayoutParams().height = eqImageView.getLayoutParams().height + random;
            viewBinding.eqImageView.requestLayout()

            // Store current dB value
            currentFloatDB = rmsdB
        }
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Timber.d("onBufferReceived() : %s", buffer)
    }

    override fun onEndOfSpeech() {
        Timber.d("onEndOfSpeech()")
    }

    override fun onError(error: Int) {
        Timber.e("FAILED %s", error)

        message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> getString(R.string.error_audio_error)
            SpeechRecognizer.ERROR_CLIENT -> getString(R.string.error_client)
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> getString(R.string.error_permission)
            SpeechRecognizer.ERROR_NETWORK -> getString(R.string.error_network)
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT, SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> getString(
                R.string.error_timeout
            )
            SpeechRecognizer.ERROR_NO_MATCH -> getString(R.string.error_no_match)
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> getString(R.string.error_busy)
            SpeechRecognizer.ERROR_SERVER -> getString(R.string.error_server)
            else -> getString(R.string.error_understand)
        }

        Timber.e(message)

        if (isRecordingViewVisible()) {
            // Hide Recording view
            hideRecordingView()
            viewBinding.speechButton.setColorFilter(
                ContextCompat.getColor(
                    context!!,
                    R.color.white
                )
            )
            hideEq()
        }

        viewBinding.speechInputTextView.text = message
    }

    override fun onResults(results: Bundle?) {
        Timber.e("onResults()")

        var result = ""

        val matches = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        for (element in matches!!) Timber.d(element)

        Timber.d("User said : %s", matches[0])

        if (isRecordingViewVisible()) {
            // Hide Recording view
            hideRecordingView()
            viewBinding.speechButton.setColorFilter(
                ContextCompat.getColor(
                    context!!,
                    R.color.white
                )
            )
            hideEq()
        }

        result = matches[0]
        viewBinding.speechInputTextView.text = result
    }

    override fun onPartialResults(partialResults: Bundle?) {
        Timber.i("onPartialResults()")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Timber.i("onEvent()")
    }
}