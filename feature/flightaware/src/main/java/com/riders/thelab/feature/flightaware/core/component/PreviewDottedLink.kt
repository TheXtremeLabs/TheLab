package com.riders.thelab.feature.flightaware.core.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AirplanemodeActive
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.cardBackgroundColor

@Composable
fun DottedLink(modifier: Modifier) {
    TheLabTheme {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {

                drawCircle(
                    color = Color.White,
                    radius = 8f,
                    center = Offset(0f, this.size.height / 2)
                )

                drawLine(
                    color = Color.White,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    pathEffect = pathEffect
                )

                drawCircle(
                    color = Color.White,
                    radius = 8f,
                    center = Offset(this.size.width, this.size.height / 2)
                )
            }


            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(cardBackgroundColor)
                    .zIndex(5f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .rotate(90f)
                        .padding(8.dp),
                    imageVector = Icons.Rounded.AirplanemodeActive,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewDottedLink() {
    TheLabTheme {
        Column(modifier = Modifier.background(cardBackgroundColor)) {
            DottedLink(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}