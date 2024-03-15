package com.riders.thelab.core.ui.compose.component.tickerborad

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
fun TickerBoard(
    // 1
    text: String,
    // 2
    numColumns: Int,
    // 3
    numRows: Int,
    modifier: Modifier = Modifier,
    textColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.LightGray,
    backgroundColor: Color = if (!isSystemInDarkTheme()) Color.White else Color.Black,
    fontSize: TextUnit = 48.sp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    containsCarriageReturn: Boolean = text.contains("\n")
) {

    // 4
    val padded = text.padEnd(numColumns * numRows, ' ')

    if (!containsCarriageReturn) {
        // 5
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement,
        ) {
            // 6
            repeat(numRows) { row ->
                TickerRow(
                    text = padded.substring(startIndex = row * numColumns),
                    numCells = numColumns,
                    horizontalArrangement = horizontalArrangement,
                    textColor = textColor,
                    backgroundColor = backgroundColor,
                    fontSize = fontSize,
                )
            }
        }
    } else {
        // 5
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement,
        ) {

            val splitText = text.split("\n")

            // 6
            repeat(numRows) { row ->
                val paddedText = splitText[row].padEnd(numColumns * numRows, ' ')

                TickerRow(
                    text = paddedText,
                    numCells = numColumns,
                    horizontalArrangement = horizontalArrangement,
                    textColor = textColor,
                    backgroundColor = backgroundColor,
                    fontSize = fontSize,
                )
            }
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
private fun PreviewTickerBoard() {
    TheLabTheme {
        TickerBoard("This is a Ticker Board", 5, 3, Modifier.fillMaxWidth())
    }
}

@DevicePreviews
@Composable
private fun PreviewTickerBoardWithCarriageReturn() {
    TheLabTheme {
        TickerBoard("This is a\nTicker Board", 5, 3, Modifier.fillMaxWidth())
    }
}