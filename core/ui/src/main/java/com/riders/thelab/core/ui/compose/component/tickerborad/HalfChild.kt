package com.riders.thelab.core.ui.compose.component.tickerborad

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun HalfChild(
    modifier: Modifier = Modifier,
    topHalf: Boolean = true,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier.clipToBounds(),
        content = content,
    ) { measurables, constraints ->
        require(measurables.size == 1) { "This composable expects a single child" }

        val placeable = measurables.first().measure(constraints)
        val height = placeable.height / 2

        layout(
            width = placeable.width,
            height = height,
        ) {
            placeable.placeRelative(
                x = 0,
                y = if (topHalf) 0 else -height,
            )
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@Composable
private fun PreviewHalfChild() {
    TheLabTheme {
        HalfChild(modifier = Modifier.height(72.dp)) {
            CenteredText(modifier = Modifier.fillMaxHeight(), letter = 'C')
        }
    }
}