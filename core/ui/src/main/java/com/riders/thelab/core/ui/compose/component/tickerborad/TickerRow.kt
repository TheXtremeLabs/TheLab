package com.riders.thelab.core.ui.compose.component.tickerborad

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TickerRow(
    text: String,
    numCells: Int,
    modifier: Modifier = Modifier,
    textColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.LightGray,
    backgroundColor: Color = if (!isSystemInDarkTheme()) Color.White else Color.Black,
    fontSize: TextUnit = 32.sp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        repeat(numCells) { index ->
            Ticker(
                letter = text.getOrNull(index) ?: ' ',
                textColor = textColor,
                backgroundColor = backgroundColor,
                fontSize = fontSize
            )
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewTickerRow() {
    TheLabTheme {
        TickerRow("This is a Ticker Row", 20, Modifier.fillMaxSize())
    }
}