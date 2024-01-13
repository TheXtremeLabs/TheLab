package com.riders.thelab.core.player

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.riders.thelab.core.ui.compose.utils.findActivity
import timber.log.Timber

@Composable
fun YoutubeVideoPlayer(
    modifier: Modifier = Modifier,
    youtubeURL: String?,
    isPlaying: (Boolean) -> Unit = {},
    isLoading: (Boolean) -> Unit = {},
    onVideoEnded: () -> Unit = {}
) {
    val mContext = LocalContext.current
    val mLifeCycleOwner = LocalLifecycleOwner.current
    val videoId = splitLinkForVideoId(youtubeURL)
    var mYoutubePlayer: YouTubePlayer? = null
    val mYoutubePlayerView = YouTubePlayerView(mContext)

    val playerStateListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            super.onReady(youTubePlayer)
            mYoutubePlayer = youTubePlayer
            youTubePlayer.loadVideo(videoId, 0f)
        }

        override fun onStateChange(
            youTubePlayer: YouTubePlayer,
            state: PlayerConstants.PlayerState
        ) {
            super.onStateChange(youTubePlayer, state)
            when (state) {
                PlayerConstants.PlayerState.BUFFERING -> {
                    isLoading.invoke(true)
                    isPlaying.invoke(false)
                }

                PlayerConstants.PlayerState.PLAYING -> {
                    isLoading.invoke(false)
                    isPlaying.invoke(true)
                }

                PlayerConstants.PlayerState.ENDED -> {
                    isPlaying.invoke(false)
                    isLoading.invoke(false)
                    onVideoEnded.invoke()
                }

                else -> {}
            }
        }

        override fun onError(
            youTubePlayer: YouTubePlayer,
            error: PlayerConstants.PlayerError
        ) {
            super.onError(youTubePlayer, error)
            Timber.e("iFramePlayer Error Reason = $error")
        }
    }

    val playerBuilder = IFramePlayerOptions.Builder().apply {
        controls(1)
        fullscreen(0)
        autoplay(0)
        rel(1)
    }

    AndroidView(
        modifier = modifier.background(Color.DarkGray),
        factory = {
            mYoutubePlayerView.apply {
                enableAutomaticInitialization = false
                initialize(playerStateListener, playerBuilder.build())
            }
        }
    )

    DisposableEffect(key1 = Unit, effect = {
        mContext.findActivity() ?: return@DisposableEffect onDispose {}
        onDispose {
            mYoutubePlayerView.removeYouTubePlayerListener(playerStateListener)
            mYoutubePlayerView.release()
            mYoutubePlayer = null
        }
    })

    DisposableEffect(mLifeCycleOwner) {
        val lifecycle = mLifeCycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    mYoutubePlayer?.play()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    mYoutubePlayer?.pause()
                }

                else -> {
                    //
                }
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

private fun splitLinkForVideoId(
    url: String?
): String {
    return (url!!.split("="))[1]
}