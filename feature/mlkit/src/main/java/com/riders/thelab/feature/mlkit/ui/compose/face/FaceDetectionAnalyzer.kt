package com.riders.thelab.feature.mlkit.ui.compose.face

import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import timber.log.Timber

class FaceDetectionAnalyzer(
    private val previewViewWidth: Float,
    private val previewViewHeight: Float,
    private val onFaceDetected: (Rect?) -> Unit
) : ImageAnalysis.Analyzer {


    // High-accuracy landmark detection and face classification
    private val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    // Real-time contour detection
    val realTimeOpts = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    /**
     * This parameters will handle preview box scaling
     */
    private var scaleX = 1f
    private var scaleY = 1f

    private fun translateX(x: Float) = x * scaleX
    private fun translateY(y: Float) = y * scaleY

    private fun adjustBoundingRect(rect: Rect) = RectF(
        translateX(rect.left.toFloat()),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat()),
        translateY(rect.bottom.toFloat())
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (null != mediaImage) {

            // Update scale factors
            scaleX = previewViewWidth / mediaImage.height.toFloat()
            scaleY = previewViewHeight / mediaImage.width.toFloat()

            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            val detector = FaceDetection.getClient(highAccuracyOpts)
            detector
                .process(image)
                .addOnSuccessListener { faces ->
                    // Task completed successfully

                    if (faces.isEmpty()) {
                        Timber.e("process() | addOnSuccessListener | no barcodes")
                        // Remove bounding rect
                        onFaceDetected(null)
                        return@addOnSuccessListener
                    }

                    for (face in faces) {
                        val bounds: Rect = face.boundingBox
                        val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                        // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                        // nose available):
                        val leftEar: FaceLandmark? = face.getLandmark(FaceLandmark.LEFT_EAR)
                        leftEar?.let {
                            val leftEarPos: PointF = leftEar.position
                        }

                        // If contour detection was enabled:
                        val leftEyeContour: MutableList<PointF>? =
                            face.getContour(FaceContour.LEFT_EYE)?.points
                        val upperLipBottomContour: MutableList<PointF>? =
                            face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                        // If classification was enabled:
                        if (face.smilingProbability != null) {
                            val smileProb = face.smilingProbability
                        }
                        if (face.rightEyeOpenProbability != null) {
                            val rightEyeOpenProb = face.rightEyeOpenProbability
                        }

                        // If face tracking was enabled:
                        if (face.trackingId != null) {
                            val id: Int? = face.trackingId
                        }

                        onFaceDetected(bounds)
                    }
                }
                .addOnFailureListener { exception ->
                    // Task failed with an exception
                    exception.printStackTrace()
                    Timber.e("process() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
                }
                .addOnCompleteListener { faces ->
                    // Task completed
                    Timber.e("process() | addOnCompleteListener | message: ${faces.result.joinToString { face: Face? -> face.toString() + ", " }}")

                    mediaImage.close()
                    imageProxy.close()
                }
        }
    }
}