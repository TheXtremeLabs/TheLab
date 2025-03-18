package com.riders.thelab.core.ui.compose.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@Composable
fun BiColorCard(firstColor: Color, secondColor: Color) {
    Card(
        modifier = Modifier.size(24.dp),
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(firstColor)
                .rotate(135f)
        ) {
            Canvas(
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
                    .clipToBounds()
            ) {
                drawArc(
                    color = secondColor,
                    -180f,
                    180f,
                    useCenter = false,
                    size = Size(size.width, size.height * 2),
                    // style = Stroke(8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}

/////////////////////////////////////
//
// PREVIEWS
//
/////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewBiColorCard(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        BiColorCard(
            firstColor = appTheme.primaryColor,
            secondColor = appTheme.secondaryColor
        )
    }
}