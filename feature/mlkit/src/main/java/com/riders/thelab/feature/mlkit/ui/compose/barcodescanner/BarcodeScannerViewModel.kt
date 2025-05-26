package com.riders.thelab.feature.mlkit.ui.compose.barcodescanner

import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BarcodeScannerViewModel @Inject constructor(
    val uiRepository: IUiRepository
) : BaseViewModel() {

    /*var showCamera: Boolean by mutableStateOf(false)
    private set*/
    private var showCamera: MutableStateFlow<Boolean> = MutableStateFlow(false)
        private set

    fun updateShowCamera(showCamera: Boolean) {
        this.showCamera.update { showCamera }
    }

}