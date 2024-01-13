package com.riders.thelab.core.player

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.HttpDataSource
import androidx.media3.ui.PlayerView
import timber.log.Timber
import java.net.UnknownHostException


@SuppressLint("OpaqueUnitKey")
@OptIn(UnstableApi::class)
@Composable
fun ExoPlayer(modifier: Modifier = Modifier, url: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var handler: Handler? = Handler(Looper.getMainLooper())

    val exoPlayer = androidx.media3.exoplayer.ExoPlayer
        .Builder(context)
        .build()
        .also { exoPlayer ->
            // Build the media item.
            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .build()

            // Set the media item to be played.
            exoPlayer.setMediaItem(mediaItem)

            exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING

            // Add Listener
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)

                    if (playbackState == Player.STATE_ENDED) {
                        Timber.e("Video ended")
                    }

                    if (playbackState == Player.STATE_READY) {
                        Timber.e("Player is ready")
                        fun checkPlaybackPosition(delayMs: Long): Boolean =
                            handler?.postDelayed(
                                {
                                    val currentPosition = exoPlayer.currentPosition
                                    // Update UI based on currentPosition
                                    // Timber.d("Update UI based on currentPosition | position: $currentPosition")
                                    checkPlaybackPosition(delayMs)
                                },
                                delayMs
                            ) ?: false

                        checkPlaybackPosition(1_000L)
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        // Active playback.
                    } else {
                        // Not playing because playback is paused, ended, suppressed, or the player
                        // is buffering, stopped or failed. Check player.playWhenReady,
                        // player.playbackState, player.playbackSuppressionReason and
                        // player.playerError for details.
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    val cause = error.cause
                    Timber.e("onPlayerError() | message: $cause")

                    if (cause is HttpDataSource.HttpDataSourceException) {
                        // An HTTP error occurred.
                        val httpError = cause
                        // It's possible to find out more about the error both by casting and by querying
                        // the cause.
                        if (httpError is HttpDataSource.InvalidResponseCodeException) {
                            // Cast to InvalidResponseCodeException and retrieve the response code, message
                            // and headers.
                        } else {
                            // Try calling httpError.getCause() to retrieve the underlying cause, although
                            // note that it may be null.

                            if (httpError is UnknownHostException) {
                                Timber.e("No connection please your internet connection")
                            }
                        }
                    }
                }
            })
        }

    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = { factoryContext ->
                PlayerView(factoryContext).apply {
                    // Bind the player to the view.
                    this.player = exoPlayer
                    this.controllerAutoShow = true
                    this.controllerHideOnTouch = true
                }
            }
        ) { playerView ->
            playerView.player?.let { player: Player ->
                // Prepare the player.
                player.prepare()
                player.playWhenReady = true
                // Start the playback.
                player.play()
            }
        }
    ) {
        onDispose {
            if (exoPlayer.isPlaying) {
                exoPlayer.stop()
            }
            handler = null
            exoPlayer.release()
        }
    }
}

@SuppressLint("OpaqueUnitKey")
@OptIn(UnstableApi::class)
@Composable
fun ExoPlayer(modifier: Modifier = Modifier, uri: Uri) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var handler: Handler? = Handler(Looper.getMainLooper())

    val exoPlayer = androidx.media3.exoplayer.ExoPlayer
        .Builder(context)
        .build()
        .also { exoPlayer ->
            // Build the media item.
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .build()

            // Set the media item to be played.
            exoPlayer.setMediaItem(mediaItem)

            exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING

            // Add Listener
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)

                    if (playbackState == Player.STATE_ENDED) {
                        Timber.e("Video ended")
                    }

                    if (playbackState == Player.STATE_READY) {
                        Timber.e("Player is ready")
                        fun checkPlaybackPosition(delayMs: Long): Boolean =
                            handler?.postDelayed(
                                {
                                    val currentPosition = exoPlayer.currentPosition
                                    // Update UI based on currentPosition
                                    // Timber.d("Update UI based on currentPosition | position: $currentPosition")
                                    checkPlaybackPosition(delayMs)
                                },
                                delayMs
                            ) ?: false

                        checkPlaybackPosition(1_000L)
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        // Active playback.
                    } else {
                        // Not playing because playback is paused, ended, suppressed, or the player
                        // is buffering, stopped or failed. Check player.playWhenReady,
                        // player.playbackState, player.playbackSuppressionReason and
                        // player.playerError for details.
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    val cause = error.cause
                    Timber.e("onPlayerError() | message: $cause")

                    if (cause is HttpDataSource.HttpDataSourceException) {
                        // An HTTP error occurred.
                        val httpError = cause
                        // It's possible to find out more about the error both by casting and by querying
                        // the cause.
                        if (httpError is HttpDataSource.InvalidResponseCodeException) {
                            // Cast to InvalidResponseCodeException and retrieve the response code, message
                            // and headers.
                        } else {
                            // Try calling httpError.getCause() to retrieve the underlying cause, although
                            // note that it may be null.

                            if (httpError is UnknownHostException) {
                                Timber.e("No connection please your internet connection")
                            }
                        }
                    }
                }
            })
        }

    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = { factoryContext ->
                PlayerView(factoryContext).apply {
                    // Bind the player to the view.
                    this.player = exoPlayer
                    this.controllerAutoShow = true
                    this.controllerHideOnTouch = true
                }
            }
        ) { playerView ->
            playerView.player?.let { player: Player ->
                // Prepare the player.
                player.prepare()
                player.playWhenReady = true
                // Start the playback.
                player.play()
            }
        }
    ) {
        onDispose {
            if (exoPlayer.isPlaying) {
                exoPlayer.stop()
            }
            handler = null
            exoPlayer.release()
        }
    }
}