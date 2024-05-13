package com.riders.thelab.feature.download

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.riders.thelab.core.data.local.model.compose.Download
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.success
import timber.log.Timber

////////////////////////////////////////
//
// COMPOSE
//
////////////////////////////////////////
@Composable
fun DownloadItem(modifier: Modifier, item: Download) {
    Timber.d("Recomposition | DownloadItem() | progress: ${item.progress}")

    val progress = remember { mutableFloatStateOf(0f) }

    val animatedProgress: Float by animateFloatAsState(
        targetValue = progress.floatValue,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "progress bar animation progress value"
    )

    TheLabTheme {
        Card(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .padding(end = 8.dp),
                    text = item.filename,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                AnimatedContent(
                    targetState = item.isComplete,
                    label = "complete download transition",
                    contentAlignment = Alignment.Center
                ) { target ->
                    if (!target) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .zIndex(3f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (LocalInspectionMode.current) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .zIndex(3f),
                                    progress = { .45f }
                                )
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .zIndex(3f),
                                    progress = { animatedProgress }
                                )
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier.size(30.dp),
                            colors = CardDefaults.cardColors(containerColor = if (null != item.isError) com.riders.thelab.core.ui.compose.theme.error else success),
                            shape = CircleShape
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    imageVector = if (null != item.isError) Icons.Rounded.Close else Icons.Rounded.Check,
                                    contentDescription = "check_icon",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(item.progress) {
        progress.floatValue = (item.progress / 100).toFloat()
    }
}


////////////////////////////////////////
//
// PREVIEWS
//
////////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewDownloadItem(@PreviewParameter(PreviewProvider::class) item: Download) {
    TheLabTheme {
        DownloadItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            item = item
        )
    }
}