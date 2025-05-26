package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import android.graphics.RectF
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.riders.mlkitcompose.core.mlkit.BaseAnalyzer
import com.riders.thelab.feature.mlkit.data.local.model.OverlayDataFrameInfo
import com.riders.thelab.feature.mlkit.data.local.model.TextRecognitionModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextRecognitionAnalyzer(
    private val previewViewWidth: Float,
    private val previewViewHeight: Float,
    private val onTextDetected: (TextRecognitionModel?) -> Unit,
    private val onTextDetectedBoundingBoxes: ((List<OverlayDataFrameInfo>?) -> Unit)? = null,
    private val onCallback: (InputImage) -> Unit
) : BaseAnalyzer(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    private val scope: CoroutineScope = CoroutineScope(coroutineContext)
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        Timber.e("analyze()")
        scope.launch {

            val mediaImage = imageProxy.image ?: run { imageProxy.close(); return@launch }

            // Update scale factors
            scaleX = previewViewWidth / mediaImage.height.toFloat()
            scaleY = previewViewHeight / mediaImage.width.toFloat()

            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            Timber.e("analyze() | image: ${image.mediaImage.toString()}, size: ${image.width}x${image.height}")

            suspendCoroutine { continuation ->
                // Pass image to an ML Kit Vision API

                recognizer
                    .process(image)
                    .addOnSuccessListener { visionText ->
                        // Task completed successfully
                        Timber.d("process() | addOnSuccessListener | message: ${visionText.textBlocks.joinToString { textBlocks: TextBlock -> textBlocks.lines.joinToString { line -> line.text.toString() + ", " } }}")

                        val text: String = visionText.text
                        val blocks: List<TextBlock> = visionText.textBlocks
                        val lines: List<List<Text.Line>> = visionText.textBlocks.map { it.lines }


                        if (text.isBlank()) {
                            onTextDetected(null)
                            onTextDetectedBoundingBoxes?.invoke(null)
                        } else {
                            onTextDetected(TextRecognitionModel(text, blocks, lines))
                            onTextDetectedBoundingBoxes?.invoke(
                                blocks.map { block ->
                                    OverlayDataFrameInfo(
                                        block.boundingBox?.let { adjustBoundingRect(it) }
                                            ?: RectF()
                                    )
                                })
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Task failed with an exception
                        exception.printStackTrace()
                        Timber.e("process() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
                    }
                    .addOnCompleteListener {
                        Timber.d("process() | addOnCompleteListener | message: ${it.result.textBlocks.joinToString { textBlocks: TextBlock -> textBlocks.lines.joinToString { line -> line.text.toString() + ", " } }}")
                        continuation.resume(Unit)
                    }
            }

            delay(DELAY_THROTTLE)
        }.invokeOnCompletion { exception ->
            exception?.let {
                Timber.e("process() | invokeOnCompletion | message: ${it.message} (class: ${it::class.java.canonicalName})")
            }
            imageProxy.close()
        }
    }

    companion object {
        const val DELAY_THROTTLE: Long = 1_000L
    }
}