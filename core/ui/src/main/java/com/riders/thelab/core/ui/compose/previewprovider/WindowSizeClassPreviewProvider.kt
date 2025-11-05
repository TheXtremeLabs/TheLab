package com.riders.thelab.core.ui.compose.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.WindowSizeClass

class WindowSizeClassPreviewProvider : PreviewParameterProvider<WindowSizeClass> {
    override val values: Sequence<WindowSizeClass> get() = sequenceOf(
            WindowSizeClass.COMPACT,
            WindowSizeClass.MEDIUM,
            WindowSizeClass.EXPANDED
        )
}