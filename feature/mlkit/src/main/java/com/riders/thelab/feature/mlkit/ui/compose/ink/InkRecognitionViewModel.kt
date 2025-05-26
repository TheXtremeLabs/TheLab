package com.riders.thelab.feature.mlkit.ui.compose.ink

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.riders.thelab.feature.mlkit.data.local.compose.ink.InkRecognitionState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.core.ui.utils.encodeToBase64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InkRecognitionViewModel @Inject constructor(val uiRepository: IUiRepository) : BaseViewModel() {
    ////////////////////////////////////////////
    // Variables
    ////////////////////////////////////////////
    private var mInkManager: InkManager? = null

    ////////////////////////////////////////////
    // Compose States
    ////////////////////////////////////////////
    private var _inkRecognitionState: MutableStateFlow<String> = MutableStateFlow("")
    val inkRecognitionState: StateFlow<String> = _inkRecognitionState.asStateFlow()

    private fun updateInkRecognitionState(newValue: String) {
        this._inkRecognitionState.value = newValue
    }


    ////////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////////
    fun initInkManager(activity: InkRecognitionActivity) {
        Timber.d("initInkManager()")

        if (null == mInkManager) {
            mInkManager = InkManager(activity)
        }
    }

    // Call this each time there is a new event.
    fun addNewTouchEvent(offset: Offset, motionEvent: Int) {
        Timber.d("addNewTouchEvent() | offset: $offset | motionEvent: $motionEvent")
        mInkManager?.addNewTouchEvent(offset, motionEvent)
    }

    fun recognize(activity: InkRecognitionActivity) {
        Timber.d("recognize()")

        viewModelScope.launch(Dispatchers.Main) {
            mInkManager
                ?.recognize(activity)
                ?.collect {
                    Timber.d("recognize() | $it")
                    when (it) {
                        is InkRecognitionState.Recognized -> updateInkRecognitionState(it.candidate)
                        is InkRecognitionState.Failed -> {
                            Timber.d("recognize() | InkRecognitionState.Failed")
                        }
                    }
                }
        }
    }

    fun onEvent(activity: InkRecognitionActivity, event: UiEvent) {
        Timber.d("recognize()")

        when (event) {
            is UiEvent.OnSaveBitmap -> {
                val imageBitmap = event.bitmap

                viewModelScope.launch(Dispatchers.IO) {
                    Timber.d("Recomposition | imageBitmap: ${imageBitmap.width}x${imageBitmap.height}")

                    val encodedImageToString: String = imageBitmap.encodeToBase64()
                    Timber.d("Recomposition | encodedImageToString length: ${encodedImageToString.length}")

                    recognize(activity)
                }
            }

            is UiEvent.OnClearAllClicked -> {
                mInkManager?.clear()
            }

            is UiEvent.OnDismissBottomSheet -> updateInkRecognitionState("")
        }
    }
}