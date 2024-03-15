package com.riders.thelab.core.ui.compose.component.tickerborad

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.tickerborad.utils.AlphabetMapper
import com.riders.thelab.core.ui.compose.component.tickerborad.utils.TickerStateHolder
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@Composable
fun rememberTickerState() = remember { TickerStateHolder() }


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun Ticker(
    letter: Char,
    modifier: Modifier = Modifier,
    textColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.LightGray,
    backgroundColor: Color = if (!isSystemInDarkTheme()) Color.White else Color.Black,
    fontSize: TextUnit = 32.sp,
    contentPadding: PaddingValues = PaddingValues(all = 8.dp),
    state: TickerStateHolder = rememberTickerState(),
) {
    LaunchedEffect(key1 = letter) {
        val currentIndex = state.index
        val index = AlphabetMapper.getIndexOf(letter = letter, offset = currentIndex)
        state.animateTo(index)
    }

    val fraction = state.value - state.value.toInt()
    val rotation = -180f * fraction
    val currentLetter = AlphabetMapper.getLetterAt(state.index)
    val nextLetter = AlphabetMapper.getLetterAt(state.index + 1)

    Box(modifier = modifier) {
        BackgroundLetter(
            currentLetter = currentLetter,
            nextLetter = nextLetter,
            textColor = textColor,
            backgroundColor = backgroundColor,
            fontSize = fontSize,
            contentPadding = contentPadding,
        )

        Box(
            modifier = Modifier
                .graphicsLayer {
                    rotationX = rotation
                    cameraDistance = 6f * density
                    transformOrigin = TransformOrigin(.5f, 1f)
                }
        ) {
            if (fraction <= .5f) {
                TopHalf {
                    CenteredText(
                        letter = currentLetter,
                        contentPadding = contentPadding,
                        textColor = textColor,
                        backgroundColor = backgroundColor,
                        fontSize = fontSize,
                    )
                }
            } else {
                BottomHalf(modifier = Modifier.graphicsLayer { rotationX = 180f }) {
                    CenteredText(
                        letter = nextLetter,
                        contentPadding = contentPadding,
                        textColor = textColor,
                        backgroundColor = backgroundColor,
                        fontSize = fontSize,
                    )
                }
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
private fun PreviewTicker() {
    TheLabTheme {
        Ticker(modifier = Modifier.height(72.dp), letter = 'T')
    }
}