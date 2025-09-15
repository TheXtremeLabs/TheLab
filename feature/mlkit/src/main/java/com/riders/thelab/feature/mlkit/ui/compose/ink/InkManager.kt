package com.riders.thelab.feature.mlkit.ui.compose.ink

import android.content.res.Resources.NotFoundException
import android.view.MotionEvent
import androidx.compose.ui.geometry.Offset
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.common.RecognitionResult
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.recognition.Ink
import com.riders.thelab.feature.mlkit.data.local.compose.ink.InkRecognitionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class InkManager(private val activity: InkRecognitionActivity) {

    ////////////////////////////////////////////
    // Variables
    ////////////////////////////////////////////
    private var inkBuilder = Ink.builder()
    private var strokeBuilder: Ink.Stroke.Builder = Ink.Stroke.builder()

    // Ink object is a vector representation of the user’s drawing which contains the timestamp and coordinates of each stroke that the user has made.
    // This is what to send to the recognizer.
    private var ink: Ink = inkBuilder.build()

    // Specify the recognition model for a language
    private var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
    private var model: DigitalInkRecognitionModel? = null
    private val remoteModelManager = RemoteModelManager.getInstance()

    // Get a recognizer for the language
    private var recognizer: DigitalInkRecognizer? = null

    ////////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////////
    init {
        initInk()
    }

    ////////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////////

    fun initInk() {
        Timber.d("initInk()")

        try {
            Timber.d("initInk() | Fetch fromLanguageTag(en-US)...")
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        } catch (e: MlKitException) {
            // language tag failed to parse, handle error.
            e.printStackTrace()
        }
        if (null == modelIdentifier) {
            // no model was found, handle error.
            Timber.e("initInk() | Model not initialized")
            return
        } else {
            model = modelIdentifier?.let {
                Timber.d("initInk() | setup model identifier")
                DigitalInkRecognitionModel.builder(it).build()
            }
            recognizer = model?.let {
                Timber.d("initInk() | setup recognizer")

                remoteModelManager.download(it, DownloadConditions.Builder().build())
                    .addOnSuccessListener(activity) {
                        Timber.d("initInk() | addOnSuccessListener | Model downloaded")
                    }
                    .addOnFailureListener(activity) { e: Exception ->
                        e.printStackTrace()
                        Timber.e("initInk() | addOnFailureListener | Error while downloading a model: $e")
                    }

                DigitalInkRecognition.getClient(
                    DigitalInkRecognizerOptions.builder(it).build()
                )
            }
        }
    }


    // Call this each time there is a new event.
    fun addNewTouchEvent(event: MotionEvent) {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()

        // If your setup does not provide timing information, you can omit the
        // third parameter (t) in the calls to Ink.Point.create
        when (action) {
            MotionEvent.ACTION_DOWN -> {
//                strokeBuilder = Ink.Stroke.builder()
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
            }

            MotionEvent.ACTION_MOVE -> strokeBuilder.addPoint(Ink.Point.create(x, y, t))
            MotionEvent.ACTION_UP -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder.build())
            }

            else -> {
                // Action not relevant for ink construction
            }
        }
    }

    // Call this each time there is a new event.
    fun addNewTouchEvent(offset: Offset, motionEvent: Int) {
        Timber.d("addNewTouchEvent() | offset: $offset | motionEvent: $motionEvent")

        val x = offset.x
        val y = offset.y
        val t = System.currentTimeMillis()

        // If your setup does not provide timing information, you can omit the
        // third parameter (t) in the calls to Ink.Point.create
        when (motionEvent) {
            MotionEvent.ACTION_DOWN -> {
                Timber.d("addNewTouchEvent() | MotionEvent.ACTION_DOWN")

                strokeBuilder = Ink.Stroke.builder()
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
            }

            MotionEvent.ACTION_MOVE -> strokeBuilder.addPoint(Ink.Point.create(x, y, t))
            MotionEvent.ACTION_UP -> {
                Timber.d("addNewTouchEvent() | MotionEvent.ACTION_UP")

                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder.build())
                strokeBuilder = Ink.Stroke.builder()
            }

            else -> {
                // Action not relevant for ink construction
            }
        }
    }

    fun clear() {
        inkBuilder = Ink.builder()
    }


    fun recognize(activity: InkRecognitionActivity): Flow<InkRecognitionState> = callbackFlow {
        Timber.d("recognize()")

        model?.let {
            remoteModelManager
                .isModelDownloaded(it)
                .addOnSuccessListener(activity) { downloaded ->
                    if (!downloaded) {
                        Timber.e("recognize() | model is NOT downloaded")
                        trySend(
                            InkRecognitionState.Failed(
                                "Model is not downloaded",
                                NotFoundException("Model is not downloaded")
                            )
                        )
                        return@addOnSuccessListener
                    } else {
                        ink = inkBuilder.build()

                        recognizer
                            ?.recognize(ink)
                            ?.addOnSuccessListener(activity) { result: RecognitionResult ->
                                // `result` contains the recognizer's answers as a RecognitionResult.
                                val candidate = result.candidates[0].text
                                // Logs the text from the top candidate.
                                Timber.d("recognize() | addOnSuccessListener | candidate: $candidate")

                                trySend(InkRecognitionState.Recognized(candidate = candidate))
                            }
                            ?.addOnFailureListener(activity) { exception: Exception ->
                                exception.printStackTrace()
                                Timber.e("recognize() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")

                                trySend(InkRecognitionState.Failed(exception.message!!, exception))
                            }
                    }
                }
                .addOnFailureListener(activity) { exception: Exception ->
                    exception.printStackTrace()
                    Timber.e("recognize() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
                    trySend(InkRecognitionState.Failed(exception.message!!, exception))
                }
        }

        awaitClose {
            Timber.d("recognize() | awaitClose")
        }
    }
        .catch {
            it.printStackTrace()
            Timber.e("recognize() | catch | message: ${it.message} (class: ${it::class.java.canonicalName})")
        }
        .flowOn(Dispatchers.Main)

}