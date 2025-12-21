package com.riders.thelab.feature.mlkit.ui.compose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.mlkit.data.local.model.MLKitItem
import com.riders.thelab.feature.mlkit.ui.compose.utils.MLKitComposeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MLKitComposeMainViewModel @Inject constructor(
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository) {
    private lateinit var navigator: MLKitComposeNavigator

    var mlKitItems: SnapshotStateList<MLKitItem> = mutableStateListOf()

    init {
        mlKitItems.addAll(MLKitItem.mockList.toMutableList())
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    fun initNavigator(activity: MLKitComposeMainActivity) {
        navigator = MLKitComposeNavigator(activity)
    }

    fun onEvent(event: UiEvent) {
        Timber.d("onEvent() | event: $event")

        when (event) {
            is UiEvent.OnBarcodeScannerClicked -> navigator.launchBarcodeScanningActivity()
            is UiEvent.OnFaceDetectionClicked -> navigator.launchFaceDetectionActivity()
            is UiEvent.OnDocumentScannerClicked -> navigator.launchDocumentScannerActivity()
            is UiEvent.OnInkRecognitionClicked -> navigator.launchInkRecognitionActivity()
            is UiEvent.OnTextRecognitionClicked -> navigator.launchTextRecognitionActivity()
            is UiEvent.OnTranslateClicked -> navigator.launchTranslateActivity()
            is UiEvent.None -> {}
        }
    }

    fun swap(fromIdx: Int, toIdx: Int) {
        if (toIdx > fromIdx) {
            for (i in fromIdx until toIdx) {
                mlKitItems[i] = mlKitItems[i + 1].also { mlKitItems[i + 1] = mlKitItems[i] }
            }
        } else {
            for (i in fromIdx downTo toIdx + 1) {
                mlKitItems[i] = mlKitItems[i - 1].also { mlKitItems[i - 1] = mlKitItems[i] }
            }
        }
    }
}