package com.riders.thelab.core.ui.compose.component.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primaryContainer

@Composable
fun LabTabIndicator(
    indicatorWidth: Dp,
    indicatorOffset: Dp,
    indicatorColor: Color,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(width = indicatorWidth)
            .offset(x = indicatorOffset)
            .clip(shape = shape)
            .background(color = backgroundColor)
    ) {
        /*Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(.2f)
                .width(width = indicatorWidth)
                .clip(shape = shape)
                .background(color = indicatorColor)
        )*/
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
                indicatorColor = md_theme_dark_primaryContainer
            )
        }
    }
}