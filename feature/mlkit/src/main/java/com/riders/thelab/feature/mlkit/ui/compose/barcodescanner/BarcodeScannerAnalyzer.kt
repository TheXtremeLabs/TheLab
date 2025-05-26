package com.riders.thelab.feature.mlkit.ui.compose.barcodescanner

import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.riders.thelab.feature.mlkit.data.local.model.BarcodeField
import com.riders.thelab.feature.mlkit.data.local.model.BarcodeFrameInfo
import timber.log.Timber

class BarcodeScannerAnalyzer(
    private val previewViewWidth: Float,
    private val previewViewHeight: Float,
    val onBarcodeInfo: (BarcodeFrameInfo?) -> Unit,
    val onBarcode: (BarcodeField) -> Unit
) : ImageAnalysis.Analyzer {

    var viewReferenced: Int = -1

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

        val scanner = initScanner()

        val mediaImage = imageProxy.image
        if (mediaImage != null) {

            // Update scale factors
            scaleX = previewViewWidth / mediaImage.height.toFloat()
            scaleY = previewViewHeight / mediaImage.width.toFloat()

            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Pass image to an ML Kit Vision API
            scanner
                .process(image)
                .addOnSuccessListener { barcodes ->

                    if (barcodes.isEmpty()) {
                        Timber.e("process() | addOnSuccessListener | no barcodes")
                        // Remove bounding rect
                        onBarcodeInfo(null)
                        return@addOnSuccessListener
                    }

                    for (barcode in barcodes) {
                        val bounds: Rect? = barcode.boundingBox
                        val corners: Array<out Point>? = barcode.cornerPoints

                        val rawValue = barcode.rawValue.also {
                            Timber.d("process() | rawValue : $it")
                        }

                        // Update bounding rect
                        bounds?.let { rect ->
                            onBarcodeInfo(BarcodeFrameInfo(adjustBoundingRect(rect)))
                        }

                        val valueType = barcode.valueType

                        // See API reference for complete list of supported types
                        when (valueType) {
                            Barcode.TYPE_WIFI -> {
                                val ssid = barcode.wifi!!.ssid
                                val password = barcode.wifi!!.password
                                val type = barcode.wifi!!.encryptionType
                            }

                            Barcode.TYPE_URL -> {
                                val title = barcode.url!!.title
                                val url = barcode.url!!.url
                            }
                        }

                        if (null != rawValue) {
                            onBarcode(
                                BarcodeField(
                                    type = when (valueType) {
                                        Barcode.FORMAT_ALL_FORMATS -> "all"
                                        Barcode.TYPE_UNKNOWN -> "unknown"
                                        Barcode.FORMAT_CODE_128 -> "Code 128"
                                        Barcode.TYPE_CONTACT_INFO -> "Contact Info"
                                        Barcode.TYPE_EMAIL -> "Email"
                                        Barcode.FORMAT_CODE_39 -> "Code 39"
                                        Barcode.TYPE_PHONE -> "Phone"
                                        Barcode.FORMAT_CODE_93 -> "Code 93"
                                        Barcode.TYPE_URL -> "Url"
                                        Barcode.FORMAT_CODABAR -> "Code a bar"
                                        Barcode.FORMAT_DATA_MATRIX -> "Data Matrix"
                                        Barcode.FORMAT_EAN_13 -> "EAN 13"
                                        Barcode.FORMAT_EAN_8 -> "EAN 8"
                                        Barcode.FORMAT_ITF -> "ITF"
                                        Barcode.FORMAT_QR_CODE -> "QR Code"
                                        Barcode.FORMAT_UPC_A -> "UPC A"
                                        Barcode.FORMAT_UPC_E -> "UPC E"
                                        Barcode.FORMAT_PDF417 -> "PDF"
                                        Barcode.FORMAT_AZTEC -> "Aztec"
                                        Barcode.TYPE_ISBN -> "ISBN"
                                        Barcode.TYPE_PRODUCT -> "Product"
                                        Barcode.TYPE_SMS -> "SMS"
                                        Barcode.TYPE_TEXT -> "Text"
                                        Barcode.TYPE_WIFI -> "WIFI"
                                        Barcode.TYPE_GEO -> "GEO"
                                        Barcode.TYPE_CALENDAR_EVENT -> "Calendar"
                                        Barcode.TYPE_DRIVER_LICENSE -> "Driver License"
                                        Barcode.FORMAT_UNKNOWN -> "Format unknown"
                                        else -> "Format unknown"
                                    },
                                    value = rawValue
                                )
                            )
                        }
                    }
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    it.printStackTrace()
                    Timber.e("process() | addOnFailureListener | message: ${it.message} (class: ${it::class.java.canonicalName})")
                }
                .addOnCompleteListener {
                    Timber.e("process() | addOnCompleteListener | message: ${it.result.joinToString { barcode: Barcode? -> barcode?.rawValue.toString() }}")

                    mediaImage.close()
                    imageProxy.close()
                }
        }
    }

    private fun initScanner(): BarcodeScanner {

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .enableAllPotentialBarcodes() // Optional
            .setZoomSuggestionOptions(
                ZoomSuggestionOptions.Builder { true }
                    .setMaxSupportedZoomRatio(BarcodeScannerUtils.maxSupportedZoomRatio)
                    .build()
            ) // Optional
            .build()

        val scanner = BarcodeScanning.getClient(options)

        return scanner
    }
}