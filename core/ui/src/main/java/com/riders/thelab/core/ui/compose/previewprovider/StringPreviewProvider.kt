package com.riders.thelab.core.ui.compose.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.ui.R

class VideoPathPreviewProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("android.resource://com.riders.composevideoplayer/${R.raw.splash_intro_testing_sound_2}")
}


class TextContentPreviewProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("Lorem Ipsum")
}