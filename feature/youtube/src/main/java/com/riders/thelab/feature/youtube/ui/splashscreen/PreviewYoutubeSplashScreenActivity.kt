package com.riders.thelab.feature.youtube.ui.splashscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.utils.UIManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun YoutubeSplashScreenContent() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /* Convert our Image Resource into a Bitmap */
    val bitmap = remember { UIManager.getDrawableAsBitmap(context, R.drawable.youtube_icon_like)!! }

    /* Create the Palette, pass the bitmap to it */
    var palette: Palette

    var showPlayLogoAndProgressBar by remember { mutableStateOf(true) }
    var showYoutubeLogo by remember { mutableStateOf(false) }
    var showProgressBar by remember { mutableStateOf(false) }
    val animatePlayIcon by remember { mutableStateOf(false) }
    var animatePlayLogoRotation by remember { mutableStateOf(false) }

    val playLogoScaleAnimation = remember { Animatable(2f) }
    val playLogoRotationAnimation by animateFloatAsState(
        targetValue = if (!animatePlayLogoRotation) 0f else 360f,
        label = "play_logo_rotation_animation",
        animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing),
        finishedListener = {
            scope.launch {
                showProgressBar = true
                delay(1_550L)
                showProgressBar = false

                delay(350L)

                showYoutubeLogo = true
                delay(150L)
                showPlayLogoAndProgressBar = false
            }
        })

    /* Get the swatches */
    var darkVibrantSwatch by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        scope.launch {

            palette = Palette.from(bitmap).generate()

            ////////////////

            darkVibrantSwatch = palette.darkVibrantSwatch?.rgb ?: 0
        }
    }

    TheLabTheme {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(darkVibrantSwatch)),
            contentAlignment = Alignment.Center
        ) {

            AnimatedVisibility(
                visible = showPlayLogoAndProgressBar,
                label = "Loading_animation"
            ) {
                Row(
                    modifier = Modifier.widthIn(40.dp, this@BoxWithConstraints.maxWidth - 64.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .scale(playLogoScaleAnimation.value)
                            .rotate(playLogoRotationAnimation),
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )

                    AnimatedVisibility(
                        visible = if (LocalInspectionMode.current) true else showProgressBar,
                        exit = shrinkHorizontally() + fadeOut(),
                        label = "progress_bar_animation"
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(.7f),
                                trackColor = Color.White.copy(alpha = 0.583f),
                                color = Color.White,
                                strokeCap = StrokeCap.Round
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = if (LocalInspectionMode.current) true else showYoutubeLogo,
                enter = fadeIn() + scaleIn(),
                label = "progress_bar_animation"
            ) {
                Image(
                    modifier = Modifier
                        .size(96.dp)
                        .alpha(if (LocalInspectionMode.current) .65f else 1f)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        ),
                    painter = painterResource(id = R.drawable.youtube_icon_like),
                    contentDescription = null
                )
            }
        }
    }

    LaunchedEffect(animatePlayIcon) {
        delay(100L)
        playLogoScaleAnimation.animateTo(2.5f)
        delay(50L)
        playLogoScaleAnimation.animateTo(1.5f)

        delay(150L)
        animatePlayLogoRotation = true
    }

    LaunchedEffect(showPlayLogoAndProgressBar) {
        if (!showPlayLogoAndProgressBar) {
            delay(250L)
            (context.findActivity() as YoutubeSplashScreenActivity).launchYoutubeActivity()
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
private fun PreviewYoutubeSplashScreenContent() {
    TheLabTheme {
        YoutubeSplashScreenContent()
    }
}