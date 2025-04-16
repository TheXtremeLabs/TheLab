package com.riders.thelab.core.ui.compose.component.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

// Source : https://maxtayler.medium.com/jetpack-compose-ticket-shape-88de5cf0b9c5
// Source : https://medium.com/@bhoomigadhiya/mastering-shapes-in-jetpack-compose-0474d8f7fd5e
class TicketShape(
    private val circleRadius: Dp,
    private val cornerSize: CornerSize
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(path = getPath(size, density))

    private fun getPath(size: Size, density: Density): Path {
        val roundedRect = RoundRect(size.toRect(), CornerRadius(cornerSize.toPx(size, density)))
        val roundedRectPath = Path().apply { addRoundRect(roundedRect) }
        return Path.combine(
            // Using PathOperation.Intersect will ensure we only keep the overlapping sections of both paths,
            // in this case that will mean the square corners of the shape we drew earlier
            // will get cut out as they will not overlap with the rounded rectangle.
            operation = PathOperation.Intersect,
            path1 = roundedRectPath,
            path2 = getTicketPath(size, density)
        )
    }

    private fun getTicketPath(size: Size, density: Density): Path {
        val middleX = size.width.div(other = 2)
        val circleRadiusInPx = with(density) { circleRadius.toPx() }
        return Path().apply {
            reset()
            // Ensure we start drawing line at top left
            lineTo(x = 0F, y = 0F)
            // Draw line to top middle
            lineTo(middleX, y = 0F)
            // Draw top cutout
            arcTo(
                rect = Rect(
                    left = middleX.minus(circleRadiusInPx),
                    top = 0f.minus(circleRadiusInPx),
                    right = middleX.plus(circleRadiusInPx),
                    bottom = circleRadiusInPx
                ),
                startAngleDegrees = 180F,
                sweepAngleDegrees = -180F,
                forceMoveTo = false
            )
            // Draw line to top right
            lineTo(x = size.width, y = 0F)
            // Draw line to bottom right
            lineTo(x = size.width, y = size.height)
            // Draw line to bottom middle
            lineTo(middleX, y = size.height)
            // Draw bottom cutout
            arcTo(
                rect = Rect(
                    left = middleX.minus(circleRadiusInPx),
                    top = size.height - circleRadiusInPx,
                    right = middleX.plus(circleRadiusInPx),
                    bottom = size.height.plus(circleRadiusInPx)
                ),
                startAngleDegrees = 0F,
                sweepAngleDegrees = -180F,
                forceMoveTo = false
            )
            // Draw line to bottom left
            lineTo(x = 0F, y = size.height)
            // Draw line back to top left
            lineTo(x = 0F, y = 0F)
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewTicketShape(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(450.dp)
                .clip(TicketShape(circleRadius = 8.dp, cornerSize = CornerSize(8.dp)))
                .background(color = Color.DarkGray.copy(alpha = .9414f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Power Book: Ghost",
                color = Color.LightGray
            )
        }
    }
}
