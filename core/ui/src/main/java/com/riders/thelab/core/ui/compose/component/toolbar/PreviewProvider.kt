package com.riders.thelab.core.ui.compose.component.toolbar

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class PreviewProviderToolbarSize : PreviewParameterProvider<ToolbarSize> {
    override val values: Sequence<ToolbarSize>
        get() = sequenceOf(
            ToolbarSize.SMALL,
            ToolbarSize.MEDIUM,
            ToolbarSize.LARGE,
        )
}

class PreviewProviderToolbarTitle : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("Palette", "Colors", "Biometric")
}