package com.riders.thelab.vision.ui.vision

import androidx.lifecycle.DefaultLifecycleObserver
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.UiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisionViewModel @Inject constructor(
    val uiRepository: UiRepository
) : BaseViewModel(), DefaultLifecycleObserver {
}