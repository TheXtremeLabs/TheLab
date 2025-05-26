package com.riders.thelab.feature.mlkit.ui.compose.face

import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.Triangle
import com.google.mlkit.vision.facemesh.FaceMesh
import com.google.mlkit.vision.facemesh.FaceMeshDetection
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions
import com.google.mlkit.vision.facemesh.FaceMeshPoint
import timber.log.Timber

class FaceMeshDetectionAnalyzer(
    private val previewViewWidth: Float,
    private val previewViewHeight: Float,
    private val onFacesDetected: (List<Rect>?) -> Unit,
    private val onMeshesBuilt: (List<FaceMeshPoint>?) -> Unit
) : ImageAnalysis.Analyzer {

    val defaultDetector = FaceMeshDetection.getClient(
        FaceMeshDetectorOptions.Builder().build()
    )

    val boundingBoxDetector = FaceMeshDetection.getClient(
        FaceMeshDetectorOptions.Builder()
            .setUseCase(FaceMeshDetectorOptions.BOUNDING_BOX_ONLY)
            .build()
    )

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
            boundingBoxDetector
                .process(image)
                .addOnSuccessListener { faceMeshes ->
                    // Task completed successfully

                    if (faceMeshes.isEmpty()) {
                        Timber.e("process() | addOnSuccessListener | no face meshes")
                        // Remove bounding rect
                        onFacesDetected(null)
                        return@addOnSuccessListener
                    }

                    for (faceMesh in faceMeshes) {
                        val bounds: Rect = faceMesh.boundingBox

                        // Gets all points
                        val faceMeshPoints = faceMesh.allPoints
                        for (faceMeshPoint in faceMeshPoints) {
                            val index: Int = faceMeshPoints.indexOf(faceMeshPoint)
                            val position = faceMeshPoint.position
                        }

                        // Gets triangle info
                        val triangles: List<Triangle<FaceMeshPoint>> = faceMesh.allTriangles
                        if (triangles.isEmpty()) {
                            onMeshesBuilt(null)
                        } else {
                            for (triangle in triangles) {
                                // 3 Points connecting to each other and representing a triangle area.
                                val connectedPoints: List<FaceMeshPoint> = triangle.allPoints
                                onMeshesBuilt(connectedPoints)
                            }
                        }
                    }

                    onFacesDetected(faceMeshes.map { it.boundingBox })
                }
                .addOnFailureListener { exception ->
                    // Task failed with an exception
                    exception.printStackTrace()
                    Timber.e("process() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
                }
                .addOnCompleteListener { faceMeshes ->
                    // Task completed
                    Timber.e("process() | addOnCompleteListener | message: ${faceMeshes.result.joinToString { face: FaceMesh? -> face.toString() + ", " }}")

                    mediaImage.close()
                    imageProxy.close()
                }
        }
    }
}