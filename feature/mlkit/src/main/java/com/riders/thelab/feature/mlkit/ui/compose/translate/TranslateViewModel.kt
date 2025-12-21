package com.riders.thelab.feature.mlkit.ui.compose.translate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.mlkit.data.local.compose.translate.TranslateDownloadModelState
import com.riders.thelab.feature.mlkit.data.local.compose.translate.TranslateResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository), DefaultLifecycleObserver {

    ////////////////////////////////////////////
    // Variables
    ////////////////////////////////////////////
    private var mTranslateManager: TranslateManager? = null

    var mLanguageOptions = TranslateManager.LANGUAGES


    ////////////////////////////////////////////
    // Compose States
    ////////////////////////////////////////////
    private val _translationDownloadModel: MutableStateFlow<TranslateDownloadModelState> =
        MutableStateFlow(TranslateDownloadModelState.Idle)
    val translationDownloadModel = _translationDownloadModel.asStateFlow()

    private val _translationResults: MutableStateFlow<TranslateResultState> =
        MutableStateFlow(TranslateResultState.Idle)
    val translationResults = _translationResults.asStateFlow()

    var mSourceLanguageSelected: String by mutableStateOf(TranslateManager.DEFAULT_SOURCE_LANGUAGE)
    var mTargetLanguageSelected: String by mutableStateOf(TranslateManager.DEFAULT_TARGET_LANGUAGE)

    var inputToTranslate: String by mutableStateOf("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    var inputToTranslateFlow: StateFlow<String> =
        snapshotFlow { inputToTranslate }
            .debounce(750)
            .distinctUntilChanged()
            .mapLatest { input ->
                if (input.isEmpty()) {
                    Timber.e("Input is empty. Cannot execute the query.")
                    return@mapLatest ""
                }

                updateTranslateResultState(TranslateResultState.Loading)

                Timber.d("inputToTranslateFlow | mapLatest | it: $input")
                translate(input)
                //  if (translationResults.value !is TranslateResultState.Translated) "" else (translationResults.value as TranslateResultState.Translated).translation
                input
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ""
            )


    private fun updateTranslateDownloadModelState(newState: TranslateDownloadModelState) {
        this._translationDownloadModel.value = newState
    }

    private fun updateTranslateResultState(newState: TranslateResultState) {
        this._translationResults.value = newState
    }

    fun updateInputToTranslate(newValue: String) {
        this.inputToTranslate = newValue
    }

    private fun updateSourceLanguage(newValue: String) {
        this.mSourceLanguageSelected = newValue
        mTranslateManager?.updateLanguageOptions(
            fromLanguage = newValue,
            toLanguage = mTargetLanguageSelected
        )

        checkIfModelIsDownloaded()
    }

    private fun updateTargetLanguage(newValue: String) {
        this.mTargetLanguageSelected = newValue
        mTranslateManager?.updateLanguageOptions(
            fromLanguage = mSourceLanguageSelected,
            toLanguage = newValue
        )

        checkIfModelIsDownloaded()
    }

    private fun switchLanguagesSelection() {
        // Store selected source
        val temp = mSourceLanguageSelected

        // Apply target to source
        updateSourceLanguage(mTargetLanguageSelected)
        // Apply stored temp (aka target) to source
        updateTargetLanguage(temp)
    }

    fun initTranslateManager(activity: TranslateActivity) {
        Timber.d("initTranslateManager()")

        if (null == mTranslateManager) {
            mTranslateManager = TranslateManager(activity)
        }

        checkIfModelIsDownloaded()
    }

    private fun checkIfModelIsDownloaded() {
        Timber.d("checkIfModelIsDownloaded()")

        viewModelScope.launch(Dispatchers.Main) {
            mTranslateManager?.downloadModelIfNeeded()?.collect { state ->

                updateTranslateDownloadModelState(state)

                when (state) {
                    is TranslateDownloadModelState.Success -> {
                        Timber.d("checkIfModelIsDownloaded() | collect | success")
                    }

                    is TranslateDownloadModelState.Failed -> {
                        Timber.e("checkIfModelIsDownloaded() | collect | ${state.message}")
                    }

                    is TranslateDownloadModelState.Idle -> {
                        Timber.e("checkIfModelIsDownloaded() | is TranslateDownloadModelState.Idle")
                    }
                }
            }
        }
    }


    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnUpdateInput -> updateInputToTranslate(event.newInput)
            is UiEvent.OnSourceLanguageChanged -> updateSourceLanguage(event.newSource)
            is UiEvent.OnTargetLanguageChanged -> updateTargetLanguage(event.newTarget)
            is UiEvent.OnSwitchLanguages -> switchLanguagesSelection()
            is UiEvent.OnTranslate -> translate(inputToTranslate)
        }
    }


    fun translate(text: String) {
        Timber.d("translate() | text: $text")

        if (_translationDownloadModel.value !is TranslateDownloadModelState.Success) {
            Timber.e("Model not downloaded")
            checkIfModelIsDownloaded()
            return
        }

        if (_translationResults.value is TranslateResultState.Translated && (_translationResults.value as TranslateResultState.Translated).translation == inputToTranslate) {
            Timber.e("Translation and input are equal")
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            mTranslateManager?.translate(text)?.collect { result ->
                Timber.d("translate() | collect | result: $result")
                updateTranslateResultState(result)
            }
        }
    }
}