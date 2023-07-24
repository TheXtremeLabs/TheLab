package com.riders.thelab.core.compose.component

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.TheLabTheme

///////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////
@Composable
fun Lottie(modifier: Modifier, @RawRes rawResId: Int) {
    val composition by
    rememberLottieComposition(spec = LottieCompositionSpec.RawRes(rawResId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    Box(modifier = Modifier.then(modifier)) {
        LottieAnimation(
            modifier = Modifier.fillMaxSize(),
            composition = composition,
            progress = { progress }
        )
    }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
fun PreviewLottie() {
    TheLabTheme {
        Lottie(modifier = Modifier, rawResId = R.raw.error_rolling_dark_theme)
    }
}