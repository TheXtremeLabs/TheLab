package com.riders.thelab.feature.mlkit.ui.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.feature.mlkit.data.local.model.MLKitItem

class PreviewProviderMLKitItem : PreviewParameterProvider<MLKitItem> {
    override val values: Sequence<MLKitItem>
        get() = sequenceOf(MLKitItem.mock)
}

class PreviewProviderMLKitListItem : PreviewParameterProvider<List<MLKitItem>> {
    override val values: Sequence<List<MLKitItem>>
        get() = sequenceOf(MLKitItem.mockList)
}