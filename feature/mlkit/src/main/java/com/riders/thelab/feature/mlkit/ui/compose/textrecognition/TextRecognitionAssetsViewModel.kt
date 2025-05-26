package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
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
class TextRecognitionAssetsViewModel @Inject constructor(
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
        Timber.d("updateTextRecognitionState() | state: ${state.javaClass.simpleName}")
        this._textRecognitionState.value = state
    }

    override fun onCleared() {
        Timber.e("onCleared()")

        if (null != mTextRecognitionManager) {
            mTextRecognitionManager?.closeRecognizer()
        }
        mTextRecognitionManager = null

        if (true == mRecognitionJob?.isActive) {
            mRecognitionJob?.cancel()
        }
        mRecognitionJob = null
        super.onCleared()
    }

    fun initRecognitionManager(activity: TextRecognitionAssetsActivity) {
        if (null == mTextRecognitionManager)
            mTextRecognitionManager = TextRecognitionManager(activity)
    }

    fun recognizeText(bitmap: Bitmap) {
        Timber.d("recognizeText()")

        mRecognitionJob = viewModelScope.launch(Dispatchers.Main) {
            mTextRecognitionManager
                ?.recognizeText(bitmap)
                ?.collect {
                    Timber.d("recognizeText() | collect | result: $it")
                    updateTextRecognitionState(it)
                }
        }
    }
}