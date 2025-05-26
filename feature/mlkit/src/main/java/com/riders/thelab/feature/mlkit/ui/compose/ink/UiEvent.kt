package com.riders.thelab.feature.mlkit.ui.compose.ink

import android.graphics.Bitmap
import androidx.compose.runtime.Stable

@Stable
sealed interface UiEvent {
    data class OnSaveBitmap(val bitmap: Bitmap):UiEvent
    data object OnClearAllClicked :UiEvent
    data object OnDismissBottomSheet :UiEvent
}