package com.riders.thelab.core.compose.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.ui.splashscreen.SplashScreenViewModel


class SplashScreenViewModelPreviewProvider : PreviewParameterProvider<SplashScreenViewModel> {
    override val values: Sequence<SplashScreenViewModel>
        get() = sequenceOf(SplashScreenViewModel())
}