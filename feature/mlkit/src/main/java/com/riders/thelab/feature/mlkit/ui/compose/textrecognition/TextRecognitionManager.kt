package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.riders.thelab.feature.mlkit.data.local.compose.textrecognition.TextRecognitionState
import com.riders.thelab.feature.mlkit.data.local.model.TextRecognitionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class TextRecognitionManager(private val activity: ComponentActivity) {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun recognizeText(bitmap: Bitmap): Flow<TextRecognitionState> = callbackFlow {
        Timber.e("recognizeText() | from bitmap image (assets)")

        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer
            .process(image)
            .addOnSuccessListener(activity) { visionText ->
                // Task completed successfully
                Timber.d("process() | addOnSuccessListener | message: ${visionText.textBlocks.joinToString { textBlocks: TextBlock -> textBlocks.lines.joinToString { line -> line.text.toString() + ", " } }}")

                val text: String = visionText.text
                val blocks: List<TextBlock> = visionText.textBlocks
                val lines: List<List<Text.Line>> = visionText.textBlocks.map { it.lines }

                trySend(TextRecognitionState.Recognized(TextRecognitionModel(text, blocks, lines)))
            }
            .addOnFailureListener(activity) { exception ->
                // Task failed with an exception
                exception.printStackTrace()
                Timber.e("process() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")

                trySend(TextRecognitionState.Error(exception.message!!, exception))
            }
            .addOnCompleteListener(activity) {
                Timber.d("process() | addOnCompleteListener | task completed")
            }

        awaitClose {
            Timber.e("recognizeText() | awaitClose | close recognizer")

            runBlocking { delay(5_000L) }
            closeRecognizer()
        }
    }
        .catch { exception ->
            // Model couldn’t be downloaded or other internal error.
            exception.printStackTrace()
            Timber.e("recognizeText() | catch | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
        }
        .flowOn(Dispatchers.Main)

    fun recognizeText(image: InputImage): Flow<TextRecognitionState> = callbackFlow {
        recognizer
            .process(image)
            .addOnSuccessListener(activity) { visionText ->
                // Task completed successfully
                Timber.d("process() | addOnSuccessListener | message: ${visionText.textBlocks.joinToString { textBlocks: TextBlock -> textBlocks.lines.joinToString { line -> line.text.toString() + ", " } }}")

                val text: String = visionText.text
                val blocks: List<TextBlock> = visionText.textBlocks
                val lines: List<List<Text.Line>> = visionText.textBlocks.map { it.lines }

                trySend(TextRecognitionState.Recognized(TextRecognitionModel(text, blocks, lines)))
            }
            .addOnFailureListener(activity) { exception ->
                // Task failed with an exception
                exception.printStackTrace()
                Timber.e("process() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")

                trySend(TextRecognitionState.Error(exception.message!!, exception))
            }
            .addOnCompleteListener(activity) {
                Timber.d("process() | addOnCompleteListener | message: ${it.result.textBlocks.joinToString { textBlocks: TextBlock -> textBlocks.lines.joinToString { line -> line.text.toString() + ", " } }}")
            }

        awaitClose {
            Timber.e("recognizeText() | awaitClose | close recognizer")
            closeRecognizer()
        }
    }
        .distinctUntilChanged()
        .catch { exception ->
            // Model couldn’t be downloaded or other internal error.
            exception.printStackTrace()
            Timber.e("recognizeText() | catch | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
        }
        .flowOn(Dispatchers.Main)

    fun closeRecognizer() {
        Timber.e("closeRecognizer()")
        recognizer.close()
    }
}