package com.riders.thelab.feature.theaters.detail

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.tmdb.TMDBVideoModel
import com.riders.thelab.core.data.utils.Constants
import com.riders.thelab.core.player.YoutubeVideoPlayer
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.theaters.previewprovider.PreviewProviderTMDBVideoModel
import timber.log.Timber

@Composable
fun PopUpTrailer(
    modifier: Modifier = Modifier,
    itemName: String,
    tmdbVideoModel: TMDBVideoModel,
    onCloseButtonClicked: (Boolean) -> Unit
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val mModifier:Modifier = when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Modifier
                .fillMaxWidth()
                .height(screenHeight / 2)
                .padding(16.dp)
        }

        Configuration.ORIENTATION_LANDSCAPE -> {
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        }

        else -> {
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        }
    }

    Timber.d("PopUpTrailer() | Recomposition | ")

    TheLabTheme {
        Card(
            modifier = mModifier.then(modifier),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
        ) {
            AnimatedContent(
                targetState = tmdbVideoModel.key.isNotBlank(),
                label = ""
            ) { targetState ->
                if (!targetState) {
                    Column {

                        Lottie(
                            modifier = Modifier.fillMaxWidth(.7f),
                            rawResId = com.riders.thelab.core.ui.R.raw.lottie_hot_coffee_loading
                        )

                        Text(text = "No data")
                    }
                } else {

                    Box(modifier = Modifier.background(Color.Transparent)) {
                        YoutubeVideoPlayer(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 16.dp),
                            youtubeURL = "${Constants.BASE_ENDPOINT_YOUTUBE_WATCH_BASE_URL}${tmdbVideoModel.key}",
                            isLoading = {
                                Timber.i("isLoading()")
                            },
                            isPlaying = {
                                Timber.d("isPlaying()")
                            },
                            onVideoEnded = {
                                Timber.e("onVideoEnded()")
                                onCloseButtonClicked(false)
                            })

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.TopStart),
                                text = itemName,
                                style = TextStyle(fontWeight = FontWeight.W700, color = Color.White)
                            )

                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(30.dp)
                                    .clip(CircleShape),
                                onClick = { onCloseButtonClicked(false) }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.9f)),
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "close icon",
                                    tint = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewPopUpTrailer(@PreviewParameter(PreviewProviderTMDBVideoModel::class) videoModel: TMDBVideoModel) {
    TheLabTheme {
        PopUpTrailer(
            modifier = Modifier.fillMaxSize(),
            itemName = "Dummy title example",
            tmdbVideoModel = videoModel,
            onCloseButtonClicked = {})
    }
}