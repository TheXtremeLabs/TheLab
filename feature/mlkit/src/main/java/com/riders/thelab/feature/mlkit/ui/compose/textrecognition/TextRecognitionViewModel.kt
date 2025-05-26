package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.riders.mlkitcompose.data.local.compose.textrecognition.TextRecognitionState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    val uiRepository: IUiRepository
) : BaseViewModel() {
    ////////////////////////////////////////////
    // Variables
    ////////////////////////////////////////////
    private var mRecognitionJob: Job? = null
    private var mTextRecognitionManager: TextRecognitionManager? = null

    ////////////////////////////////////////////
    // Compose States
    ////////////////////////////////////////////
    private val _textRecognitionState: MutableStateFlow<TextRecognitionState> =
        MutableStateFlow(TextRecognitionState.Idle)
    val textRecognitionState = _textRecognitionState.asStateFlow()

    private fun updateTextRecognitionState(state: TextRecognitionState) {
        this._textRecognitionState.value = state
    }

    override fun onCleared() {
        Timber.e("onCleared()")
        mTextRecognitionManager = null
        if (true == mRecognitionJob?.isActive) {
            mRecognitionJob?.cancel()
        }
        mRecognitionJob = null
        super.onCleared()
    }


    ////////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////////
    fun onEvent(uiEvent: UiEvent) {
        Timber.d("onEvent() | event: ${uiEvent.toString()}")

        when (uiEvent) {
            is UiEvent.OnTextRecognition -> updateTextRecognitionState(
                TextRecognitionState.Recognized(
                    uiEvent.model
                )
            )

            is UiEvent.OnDismissBottomSheet -> updateTextRecognitionState(TextRecognitionState.Idle)
            is UiEvent.StartTextRecognition -> recognizeText(uiEvent.inputImage)
            is UiEvent.OnAssetImageClicked -> {}
        }
    }

    fun initRecognitionManager(activity: TextRecognitionActivity) {
        if (null == mTextRecognitionManager)
            mTextRecognitionManager = TextRecognitionManager(activity)
    }

    private fun recognizeText(inputImage: InputImage) {
        Timber.d("recognizeText()")

        mRecognitionJob = viewModelScope.launch(Dispatchers.Main) {
            mTextRecognitionManager
                ?.recognizeText(inputImage)
                ?.collect {
                    Timber.d("recognizeText() | collect | result: $it")
                    updateTextRecognitionState(it)
                }
        }
    }
}