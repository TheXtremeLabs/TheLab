package com.riders.thelab.feature.mlkit.data.local.model

import android.graphics.RectF
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class OverlayDataFrameInfo(
    val barcodeFrameX: Int,
    val barcodeFrameY: Int,
    val barcodeFrameWidth: Int,
    val barcodeFrameHeight: Int
) {

    constructor(rect: RectF) : this(
        barcodeFrameX = rect.left.toInt(),
        barcodeFrameY = rect.top.toInt(),
        barcodeFrameWidth = rect.width().toInt(),
        barcodeFrameHeight = rect.height().toInt()
    )
}
