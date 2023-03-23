package com.riders.thelab.core.compose.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.data.local.model.compose.IslandState

class IslandStatePreviewProvider : PreviewParameterProvider<IslandState> {
    override val values: Sequence<IslandState>
        get() = sequenceOf(
            IslandState.DefaultState(),
            IslandState.WelcomeState(),
            IslandState.SearchState(),
            IslandState.CallState(),
            IslandState.FaceUnlockState(),
            IslandState.CallTimerState(),
        )
}
