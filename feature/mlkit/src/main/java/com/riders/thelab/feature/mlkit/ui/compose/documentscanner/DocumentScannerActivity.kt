package com.riders.thelab.feature.mlkit.ui.compose.documentscanner

import android.os.Bundle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import timber.log.Timber

class DocumentScannerActivity : BaseComponentActivity() {

    private val options = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(false)
        .setPageLimit(2)
        .setResultFormats(
            GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
            GmsDocumentScannerOptions.RESULT_FORMAT_PDF
        )
        .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
        .build()

    private val scanner = GmsDocumentScanning.getClient(options)
    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            Timber.e("registerForActivityResult | message: ${result.resultCode}, extras: ${result.data}")

            when (result.resultCode) {
                RESULT_OK -> {
                    Timber.e("registerForActivityResult | RESULT_OK")
                    val scanningResult =
                        GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                    if (scanningResult != null) {
                        scanningResult.pages?.let { pages ->
                            Timber.e("registerForActivityResult | scanningResult | pages: ${pages.size}")

                            for (page in pages) {
                                val imageUri =
                                    pages[0].imageUri.also { Timber.d("registerForActivityResult | scanningResult | image Uri: ${it}") }
                            }
                        }
                    }
                    if (scanningResult != null) {
                        scanningResult.pdf?.let { pdf ->
                            val pdfUri =
                                pdf.uri.also { Timber.d("registerForActivityResult | scanningResult | pdf uri: ${it}") }
                            val pageCount =
                                pdf.pageCount.also { Timber.d("registerForActivityResult | scanningResult | pdf page count: ${it}") }
                        }
                    }
                }

                RESULT_CANCELED -> {
                    Timber.e("registerForActivityResult | RESULT_CANCELED")

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scanner.getStartScanIntent(this)
            .addOnSuccessListener { intentSender ->
                scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                Timber.e("onCreate() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
            }
    }

    override fun backPressed() {
        finish()
    }
}
