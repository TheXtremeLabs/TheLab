package com.riders.thelab.core.ui.compose.component.tickerborad

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews

import kotlin.math.roundToInt

@Composable
fun CenteredText(
    letter: Char,
    modifier: Modifier = Modifier,
    textColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.LightGray,
    backgroundColor: Color = if (!isSystemInDarkTheme()) Color.White else Color.Black,
    fontSize: TextUnit = 32.sp,
    contentPadding: PaddingValues = PaddingValues(all = 8.dp)
) {
    var ascent by remember {
        mutableFloatStateOf(0f)
    }
    var middle by remember {
        mutableFloatStateOf(0f)
    }
    var baseline by remember {
        mutableFloatStateOf(0f)
    }
    var top by remember {
        mutableFloatStateOf(0f)
    }
    var bottom by remember {
        mutableFloatStateOf(0f)
    }
    val delta: Float by remember {
        derivedStateOf {
            ((bottom - baseline) - (ascent - top)) / 2f
        }
    }

    val direction = LocalLayoutDirection.current
    val startPadding = contentPadding.calculateStartPadding(direction)
    val endPadding = contentPadding.calculateEndPadding(direction)

    Text(
        modifier = modifier
            .background(backgroundColor)
            .padding(paddingValues = contentPadding)
            .drawBehind {
                drawLine(
                    textColor,
                    Offset(x = -startPadding.value * density, y = center.y),
                    Offset(
                        x = size.width + (startPadding + endPadding).value * density,
                        y = center.y
                    ),
                    strokeWidth = 2f * density,
                )
            }
            .offset {
                IntOffset(x = 0, y = delta.roundToInt())
            },
        text = letter.toString(),
        color = textColor,
        fontFamily = FontFamily.Monospace,
        fontSize = fontSize,
        onTextLayout = { textLayoutResult ->
            val layoutInput = textLayoutResult.layoutInput
            val fontSizePx = with(layoutInput.density) { layoutInput.style.fontSize.toPx() }
            baseline = textLayoutResult.firstBaseline
            top = textLayoutResult.getLineTop(0)
            bottom = textLayoutResult.getLineBottom(0)
            middle = bottom - top
            ascent = bottom - fontSizePx
        }
    )
}

@DevicePreviews
@Composable
private fun PreviewCenteredText() {
    CenteredText(letter = 'C')
}