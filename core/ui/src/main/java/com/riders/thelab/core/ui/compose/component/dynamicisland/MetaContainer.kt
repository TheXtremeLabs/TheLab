package com.riders.thelab.core.ui.compose.component.dynamicisland

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import org.intellij.lang.annotations.Language

@Language("AGSL")
const val ShaderSource = """
    uniform shader composable;
    
    uniform float cutoff;
    
    half4 main(float2 fragCoord) {
        half4 color = composable.eval(fragCoord);
        float alpha = color.a;
        if (alpha > cutoff) {
            alpha = 1.0;
        } else {
            alpha = 0.0;
        }
        
        color = half4(color.r, color.g, color.b, alpha);
        return color;
    }
"""

@SuppressLint("NewApi")
@Composable
fun MetaContainer(
    modifier: Modifier = Modifier,
    cutoff: Float = .5f,
    content: @Composable BoxScope.() -> Unit,
) {
    if (!LabCompatibilityManager.isTiramisu()) {
        Box(
            modifier.background(Color.Transparent),
            content = content
        )
    } else {
        val metaShader = remember { RuntimeShader(ShaderSource) }
        Box(
            modifier
                .background(Color.Transparent)
                .graphicsLayer {
                    metaShader.setFloatUniform("cutoff", cutoff)
                    renderEffect = RenderEffect
                        .createRuntimeShaderEffect(metaShader, "composable")
                        .asComposeRenderEffect()
                },
            content = content
        )
    }
}