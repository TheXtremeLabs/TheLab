package com.riders.thelab.feature.mlkit.ui.compose.barcodescanner

import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BarcodeScannerViewModel @Inject constructor(
    val uiRepository: IUiRepository
) : BaseViewModel() {

}