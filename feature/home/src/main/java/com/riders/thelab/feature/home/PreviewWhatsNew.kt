package com.riders.thelab.feature.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.LocalAppPreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV

///////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////
///////////////////////////////////////////////////
//
// MOBILE
//
///////////////////////////////////////////////////
///////////////////////////////////////////////////
//
// TV
//
///////////////////////////////////////////////////
@Composable
fun WhatsNewCarouselItemTV(
    theme: AppTheme,
    darkTheme: Boolean,
    localApp: LocalApp,
    index: Int,
    modifier: Modifier = Modifier,
) {
    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.CenterEnd
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = localApp.localDrawableIcon,
                contentDescription = "image_${localApp.localTitle}_$index",
                contentScale = ContentScale.FillHeight
            )

            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                androidx.tv.material3.MaterialTheme.colorScheme.background,
                                androidx.tv.material3.MaterialTheme.colorScheme.background,
                                androidx.tv.material3.MaterialTheme.colorScheme.background.copy(
                                    alpha = .833f
                                ),
                                androidx.tv.material3.MaterialTheme.colorScheme.background.copy(
                                    alpha = .5f
                                ),
                                Color.Transparent,
                            ),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )
                    )
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 48.dp,
                            vertical = 24.dp
                        )
                        .fillMaxWidth(.5f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = localApp.localTitle,
                        style = androidx.tv.material3.MaterialTheme.typography.displayMedium,
                        color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = localApp.localDescription,
                        style = androidx.tv.material3.MaterialTheme.typography.bodyMedium,
                        color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun WhatsNewContentTV(
    theme: AppTheme,
    darkTheme: Boolean,
    whatsNewList: List<LocalApp>,
    modifier: Modifier = Modifier
) {
    val carouselState = remember { CarouselState() }

    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(modifier = modifier) {
            Carousel(
                modifier = Modifier
                    .size(
                        width = this@BoxWithConstraints.maxWidth,
                        height = this@BoxWithConstraints.maxHeight
                    )
                    .padding(bottom = 32.dp),
                carouselState = carouselState,
                itemCount = whatsNewList.size,
                contentTransformStartToEnd = fadeIn(tween(durationMillis = 1000)) togetherWith fadeOut(
                    tween(durationMillis = 1000)
                ),
                contentTransformEndToStart = fadeIn(tween(durationMillis = 1000)) togetherWith fadeOut(
                    tween(durationMillis = 1000)
                ),
            ) { index ->

                whatsNewList.mapIndexed { index, localApp ->
                    WhatsNewCarouselItemTV(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 80.dp),
                        theme = theme,
                        darkTheme = darkTheme,
                        localApp = localApp,
                        index = index
                    )
                }
            }
        }
    }
}

///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviewsTV
@Composable
private fun PreviewWhatsNewCarouselItemTV() {
    val context = LocalContext.current
    val localApps: List<LocalApp> = remember {
        LocalAppPreviewProvider(context = context).values.map { it as LocalApp }.toList()
    }

    TheLabThemeTV(theme = AppTheme.Default) {
        WhatsNewCarouselItemTV(
            theme = AppTheme.Default,
            darkTheme = false,
            modifier = Modifier.height(376.dp),
            localApp = localApps.first(),
            index = 0
        )
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewWhatsNewContentTV() {
    val context = LocalContext.current
    val localApps: List<LocalApp> = remember {
        LocalAppPreviewProvider(context = context).values.map { it as LocalApp }.toList()
    }

    TheLabThemeTV(theme = AppTheme.Default) {
        WhatsNewContentTV(
            theme = AppTheme.Default,
            darkTheme = false,
            whatsNewList = localApps,
            modifier = Modifier.height(376.dp)
        )
    }
}