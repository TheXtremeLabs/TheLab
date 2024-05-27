package com.riders.thelab.feature.streaming

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.riders.thelab.core.player.ExoPlayer
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabVerticalViewPagerGeneric
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import timber.log.Timber

@Composable
fun StreamingContent() {
    val context = LocalContext.current

    val mediaList: List<String> = listOf(
        "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
        "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8",
        "https://fcc3ddae59ed.us-west-2.playback.live-video.net/api/video/v1/us-west-2.893648527354.channel.YtnrVcQbttF0.m3u8",
        "https://multiplatform-f.akamaihd.net/i/multi/will/bunny/big_buck_bunny_,640x360_400,640x360_700,640x360_1000,950x540_1500,.f4v.csmil/master.m3u8",
        "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
    )

    val pagerState: PagerState = rememberPagerState { mediaList.size }

    TheLabTheme(true) {
        Scaffold(
            topBar = {
                TheLabTopAppBar(
                    title = stringResource(id = R.string.activity_title_streaming),
                    isDarkThemeForced = true,
                    navigationIcon = {}
                )
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                LabVerticalViewPagerGeneric(
                    pagerState = pagerState,
                    items = mediaList,
                    viewPagerDotExpanded = true,
                    onUpdateViewPagerExpanded = {},
                    viewPagerDotVisibility = true,
                    onUpdateViewPagerDotVisibility = {},
                    onCurrentPageChanged = {
                        Timber.d("onCurrentPageChanged | page: $it")
                    },
                    userScrollEnabled = true
                ) { page, _ ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ExoPlayer(
                            modifier = Modifier.fillMaxWidth(),
                            url = mediaList[page],
                            hasControls = false,
                            repeatMode = 1,
                            isSourceM3u8 = mediaList[page].endsWith(".m3u8"),
                            onPlayerErrorExceptionCaught = { exception ->
                                Timber.e("onPlayerError | message: $exception")

                                exception.message?.let { it1 -> UIManager.showToast(context, it1) }
                            }
                        )
                    }
                }
            }
        }
    }
}


@DevicePreviews
@Composable
private fun PreviewStreamingContent() {
    TheLabTheme(true) {
        StreamingContent()
    }
}