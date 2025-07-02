package com.riders.thelab.core.ui.compose.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@Composable
fun NeumorphicRaisedButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE0E0E0),
    shape: RoundedCornerShape = RoundedCornerShape(30.dp),
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val density: Density = LocalDensity.current
    val interSource = remember { MutableInteractionSource() }
    val isPressed by interSource.collectIsPressedAsState()

    val shadowBlur by animateDpAsState(
        targetValue = if (isPressed) 0.dp else 8.dp
    )

    // val backgroundColor = Color(0xFFE0E0E0)
    val lightShadow = Color(0xFFFFFFFF)
    val darkShadow = Color(0xFFB1B1B1)
    // Animate shadow offset and blur radius
    // To create a hide shadow animation on press
    val upperOffset: Dp by animateDpAsState(
        targetValue = if (isPressed) 0.dp else (-10).dp
    )
    // Animate shadow offset and blur radius
    // To create a hide shadow animation on press
    val lowerLOffset: Dp by animateDpAsState(
        targetValue = if (isPressed) 0.dp else 10.dp
    )
    val radius: Dp = 15.dp
    val spread: Dp = 8.dp

    Box(
        modifier = Modifier
            /*.doubleShadowDrop(
                shape = shape
            )*/
            .fillMaxSize()
            .background(backgroundColor)
            .wrapContentSize(Alignment.Center)
            // .size(240.dp)
            .clickable(
                interactionSource = interSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
            .dropShadow(shape = shape) {
                with(density) {
                    this@dropShadow.radius = shadowBlur.value
                    this@dropShadow.color = lightShadow
                    this@dropShadow.spread = spread.value
                    this@dropShadow.offset = Offset(upperOffset.value, upperOffset.value)
                }
            }
            .dropShadow(shape = shape) {
                with(density) {
                    this@dropShadow.radius = shadowBlur.value
                    this@dropShadow.color = darkShadow
                    this@dropShadow.spread = spread.value
                    this@dropShadow.offset = Offset(lowerLOffset.value, lowerLOffset.value)
                }
            }
            .background(backgroundColor, shape)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        content.invoke()
    }
}


///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewNeumorphicRaisedButton(@PreviewParameter(AppThemePreviewProvider::class) theme: AppTheme) {
    TheLabTheme(theme = theme) {
        NeumorphicRaisedButton(
            modifier = Modifier.fillMaxWidth(.75f),
            backgroundColor = theme.primaryVariant,
            onClick = {}
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "test neumorphic button"
            )
        }
    }
}