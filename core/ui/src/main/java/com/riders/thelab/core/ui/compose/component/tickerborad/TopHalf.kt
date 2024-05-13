package com.riders.thelab.core.ui.compose.component.tickerborad

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TopHalf(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    HalfChild(
        modifier = modifier,
        topHalf = true,
        content = content,
    )
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewTopHalf() {
    TheLabTheme {
        TopHalf(modifier = Modifier.height(72.dp)) {
            CenteredText(modifier = Modifier.fillMaxHeight(), letter = 'C')
        }
    }
}
