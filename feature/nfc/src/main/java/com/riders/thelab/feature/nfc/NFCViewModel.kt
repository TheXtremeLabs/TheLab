package com.riders.thelab.feature.nfc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.nfc.LabNFCManager
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.data.local.UiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NFCViewModel @Inject constructor(
    val uiRepository: UiRepository
) : BaseViewModel(), DefaultLifecycleObserver {

    val mLabNFCManager: LabNFCManager? by lazy {
        mWeakReference
            ?.get()
            ?.let { activity ->
                LabNFCManager
                    .getInstance(
                        activity = activity.findActivity() as NFCActivity,
                        nfcReaderCallback = activity.findActivity() as NFCActivity
                    )
            }
    }


    var isCustomMessageVisible: Boolean by mutableStateOf(false)
        private set

    var customMessage: String by mutableStateOf("")
        private set

    var isScanningNFCTag: Boolean by mutableStateOf(false)
        private set

    fun updateIsCustomMessageVisible(isVisible: Boolean, message: String = "") {
        Timber.d("updateIsCustomMessageVisible() | isVisible: $isVisible | message: $message")
        this.isCustomMessageVisible = isVisible
        this.customMessage = message
    }

    private fun updateIsScanningNFCTag(isScanning: Boolean) {
        Timber.d("updateIsScanningNFCTag() | isScanning: $isScanning")
        this.isScanningNFCTag = isScanning
    }


    private var scanningNFCTagJob: Job? = null

    private val scanningNFCTagCoroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e("scanningNFCTagCoroutineExceptionHandler | Error caught with message: ${throwable.message} (class: ${throwable.javaClass.canonicalName})")
        }


    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
        cancelScanningJob()
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.StartNFCScanning -> startScanning()
            is UiEvent.StopNFCScanning -> stopScanning()
            is UiEvent.OpenSettings -> mWeakReference?.get()
                ?.let { activity -> (activity.findActivity() as NFCActivity).launchNFCSettingsIntent() }
        }
    }

    fun startScanning() {
        if (isScanningNFCTag) {
            Timber.w("startScanning() | NFC is already scanning")
            return
        }

        Timber.i("startScanning()")

        updateIsScanningNFCTag(true)

        scanningNFCTagJob =
            viewModelScope.launch(Dispatchers.IO + scanningNFCTagCoroutineExceptionHandler) {
                while (true == scanningNFCTagJob?.isActive) {

                }
            }
    }

    fun stopScanning() {
        Timber.e("stopScanning()")
        updateIsScanningNFCTag(false)
    }

    fun cancelScanningJob() {
        Timber.e("cancelScanningJob()")

        if (true == scanningNFCTagJob?.isActive) {
            scanningNFCTagJob?.cancel()
        }
        scanningNFCTagJob = null
    }
}