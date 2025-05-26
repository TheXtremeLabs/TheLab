package com.riders.mlkitcompose.core.mlkit

import android.graphics.Rect
import android.graphics.RectF
import androidx.camera.core.ImageAnalysis

abstract class BaseAnalyzer : ImageAnalysis.Analyzer {

    /**
     * This parameters will handle preview box scaling
     */
    var scaleX = 1f
    var scaleY = 1f

    private fun translateX(x: Float) = x * scaleX
    private fun translateY(y: Float) = y * scaleY

    fun adjustBoundingRect(rect: Rect) = RectF(
        translateX(rect.left.toFloat()),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat()),
        translateY(rect.bottom.toFloat())
    )
}