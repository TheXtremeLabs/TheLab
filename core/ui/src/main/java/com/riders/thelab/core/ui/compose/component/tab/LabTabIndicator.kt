package com.riders.thelab.core.ui.compose.component.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_light_primaryContainer

fun drawTicketPath(size: Size, cornerRadius: Float): Path {
    return Path().apply {
        reset()

        moveTo(x = 0f, y = cornerRadius)

        // Top left arc
        cubicTo(
            x1 = 0f, y1 = cornerRadius,
            x2 = 0f, y2 = 0f,
            x3 = cornerRadius, y3 = 0f
        )

        lineTo(x = size.width - cornerRadius, y = 0f)

        // Top right arc
        cubicTo(
            x1 = size.width - cornerRadius, y1 = 0f,
            x2 = size.width, y2 = 0f,
            x3 = size.width, y3 = cornerRadius
        )

        lineTo(x = size.width, y = size.height - cornerRadius)

        // Bottom right arc
        cubicTo(
            x1 = size.width, y1 = size.height - cornerRadius,
            x2 = size.width, y2 = size.height,
            x3 = size.width + cornerRadius, y3 = size.height
        )

        lineTo(x = 0f - cornerRadius, y = size.height)

        // Bottom left arc
        cubicTo(
            x1 = 0f - cornerRadius, y1 = size.height,
            x2 = 0f, y2 = size.height,
            x3 = 0f, y3 = size.height - cornerRadius
        )
        lineTo(x = 0f, y = cornerRadius)
        close()
    }
}

@Composable
fun LabTabIndicator(
    indicatorWidth: Dp,
    indicatorOffset: Dp,
    indicatorColor: Color = if (!isSystemInDarkTheme()) md_theme_light_primaryContainer else md_theme_dark_primaryContainer,
    hasCustomShape: Boolean = false,
    shape: Shape = if (!hasCustomShape) RoundedCornerShape(8.dp) else RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp
    )
) {
    if (!hasCustomShape) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(width = indicatorWidth)
                .offset(x = indicatorOffset)
                .clip(shape = shape)
                .background(color = indicatorColor)
        ) {}
    } else {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(width = indicatorWidth)
                .offset(x = indicatorOffset)
                .drawBehind {
                    scale(scale = 1f) {
                        val renderPath = drawTicketPath(size = size, cornerRadius = 24.dp.toPx())

                        clipPath(
                            path = renderPath,
                            clipOp = ClipOp.Intersect
                        ) {
                            drawPath(
                                path = renderPath,
                                brush = SolidColor(Color.LightGray)
                            )

                            drawRect(
                                brush = SolidColor(indicatorColor),
                                size = Size(
                                    size.width + (2 * 24.dp.toPx()),
                                    size.height
                                ),
                                topLeft = Offset(-(2 * 32f), 0f),
                            )
                        }
                    }
                }
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewLabTabIndicator() {
    TheLabTheme {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp),
            contentAlignment = Alignment.Center
        ) {
            LabTabIndicator(
                indicatorWidth = this.maxWidth / 2,
                indicatorOffset = 8.dp,
                indicatorColor = md_theme_dark_primaryContainer.copy(alpha = .65f)
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewLabTabIndicatorWithCustomShape() {
    TheLabTheme {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp),
            contentAlignment = Alignment.Center
        ) {
            LabTabIndicator(
                indicatorWidth = this.maxWidth / 2,
                indicatorOffset = 8.dp,
                indicatorColor = md_theme_dark_primaryContainer.copy(alpha = .65f),
                hasCustomShape = true
            )
        }
    }
}