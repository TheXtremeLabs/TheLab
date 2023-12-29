package com.riders.thelab.feature.download

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.Download
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.core.ui.compose.theme.success

////////////////////////////////////////
//
// COMPOSE
//
////////////////////////////////////////
@Composable
fun DownloadItem(
    modifier: Modifier,
    item: Download,
    isDbDownload: Boolean
) {
    TheLabTheme {
        Card(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Downloading file:",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.filename,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.W700,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(.5f)
                            .padding(end = 8.dp),
                        text = "${item.progress} %",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier.weight(1.5f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            progress = (item.progress / 100).toFloat()
                        )
                    }

                    Box(modifier = Modifier.weight(.5f), contentAlignment = Alignment.Center) {
                        AnimatedContent(
                            targetState = item.isComplete,
                            label = "complete download transition",
                            contentAlignment = Alignment.Center
                        ) { target ->
                            if (!target) {
                                Box(modifier = Modifier.size(30.dp))
                            } else {
                                Card(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 12.dp),
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
        }
    }
}

@Composable
fun DownloadItem(modifier: Modifier, item: Download) {
    /*val animatedProgress: Float = animateFloatAsState(
        targetValue = (item.progress / 100).toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "progress bar animation progress value"
    ).value*/

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
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            progress = (item.progress / 100).toFloat()
                        )
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
}


////////////////////////////////////////
//
// PREVIEWS
//
////////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewDownloadItem(@PreviewParameter(PreviewProvider::class) item: Download) {

    val isSamDB = item.filename.contains("bases_sam", true)
    val isAdrDb = item.filename.contains("base_adr", true)

    TheLabTheme {
        if (isSamDB || isAdrDb) {
            DownloadItem(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                isDbDownload = isSamDB || isAdrDb
            )
        } else {
            DownloadItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                item = item
            )
        }
    }
}