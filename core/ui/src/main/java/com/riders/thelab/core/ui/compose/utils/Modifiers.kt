package com.riders.thelab.core.compose.utils

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

@SuppressLint("NewApi")
fun Modifier.customBlur(blur: Float) = this.then(
    graphicsLayer {
        renderEffect = RenderEffect
            .createBlurEffect(
                blur,
                blur,
                Shader.TileMode.DECAL,
            )
            .asComposeRenderEffect()
    }
)
