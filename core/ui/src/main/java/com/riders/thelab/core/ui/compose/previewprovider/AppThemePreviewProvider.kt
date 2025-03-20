package com.riders.thelab.core.ui.compose.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.ui.compose.data.AppTheme

class AppThemePreviewProvider : PreviewParameterProvider<AppTheme> {
    override val values: Sequence<AppTheme>
        get() = sequenceOf(
            AppTheme.Default,
            AppTheme.Blue,
            AppTheme.Green,
            AppTheme.Red
        )
}