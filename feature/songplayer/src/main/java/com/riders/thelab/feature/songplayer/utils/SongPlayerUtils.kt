package com.riders.thelab.feature.songplayer.utils

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.common.MediaItem
import com.riders.thelab.core.common.storage.LabFileManager
import timber.log.Timber

object SongPlayerUtils {

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     */
    fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Float {
        val currentSeconds: Long = (currentDuration / 1000)
        val totalSeconds: Long = (totalDuration / 1000)

        // calculating percentage
        val percentage: Int = (currentSeconds.toDouble() / totalSeconds * 100).toInt()

        val floatPercentage: Float = (percentage.toFloat() / 100)
        // Timber.d("getProgressPercentage() | float percentage: $floatPercentage")

        // return percentage
        return floatPercentage
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     */
    fun progressToTimer(progress: Int, totalDuration: Int): Int {
        var mTotalDuration = totalDuration
        mTotalDuration = (mTotalDuration / 1000)
        val currentDuration: Int = (progress.toDouble() / 100 * mTotalDuration).toInt()

        // return current duration in milliseconds
        return currentDuration * 1000
    }

    fun getMediaItem(
        songTitle: String,
        songArtists: String,
        songPath: String,
        songThumbUri: String? = null
    ): MediaItem = MediaItem.Builder()
        .setMediaId("media-1")
        .setUri(songPath)
        .setMediaMetadata(
            androidx.media3.common.MediaMetadata.Builder().apply {
                setArtist(songArtists)
                setTitle(songTitle)

                songThumbUri?.let { this.setArtworkUri(Uri.parse(it)) }
            }
                .build()
        )
        .build()

    fun createMediaSession(
        context: Context,
        mp: MediaPlayer,
        songTitle: String,
        songArtists: String,
        songThumbUri: String? = null
    ): MediaSessionCompat {
        Timber.d("createMediaSession()")

        val uri = songThumbUri ?: LabFileManager.getDrawableURI(
            context,
            com.riders.thelab.core.ui.R.drawable.logo_colors
        )

        val mediaButtonReceiver = ComponentName(context, MediaButtonReceiver::class.java)
        // Create a media session. NotificationCompat.MediaStyle
        // PlayerService is your own Service or Activity responsible for media playback.
        val mediaSession =
            MediaSessionCompat(context, "PlayerService", mediaButtonReceiver, null)

        val callback: MediaSessionCompat.Callback = object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                super.onPlay()
                Timber.d("callback - onPlay()")
                if (!mp.isPlaying) mp.start()
            }

            override fun onPause() {
                super.onPause()
                Timber.e("callback - onPause()")

                if (mp.isPlaying) mp.pause()
            }

            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
                Timber.d("callback - onSeekTo()")
            }

            override fun onStop() {
                super.onStop()
                Timber.e("callback - onStop()")
                mp.stop()
            }
        }
        mediaSession.setCallback(callback)

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)

        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,

                    // Playback position.
                    // Used to update the elapsed time and the progress bar.
                    mp.currentPosition.toLong(),

                    // Playback speed.
                    // Determines the rate at which the elapsed time changes.
                    //playbackSpeed
                    1f
                )

                // isSeekable.
                // Adding the SEEK_TO action indicates that seeking is supported
                // and makes the seekbar position marker draggable. If this is not
                // supplied seek will be disabled but progress will still be shown.
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
        )

        mediaSession.setMetadata(
            MediaMetadataCompat.Builder()

                // Title.
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, songTitle)

                // Artist.
                // Could also be the channel name or TV series.
                .putString(MediaMetadata.METADATA_KEY_ARTIST, songArtists)

                // Album art.
                // Could also be a screenshot or hero image for video content
                // The URI scheme needs to be "content", "file", or "android.resource".
                .putString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI, uri)

                // Duration.
                // If duration isn't set, such as for live broadcasts, then the progress
                // indicator won't be shown on the seekbar.
                .putLong(MediaMetadata.METADATA_KEY_DURATION, mp.duration.toLong()) // 4

                .build()
        )

        return mediaSession
    }

    fun createMediaController(mediaSession: MediaSessionCompat): MediaControllerCompat {
        Timber.d("createMediaSession()")
        val controller: MediaControllerCompat = mediaSession.controller
        val controlsCallback: MediaControllerCompat.Callback =
            object : MediaControllerCompat.Callback() {
                override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                    super.onPlaybackStateChanged(state)

                    when (state?.state?.toLong()) {
                        PlaybackState.ACTION_PLAY -> {
                            Timber.d("controlsCallback -  PlaybackState.ACTION_PLAY")
                        }

                        PlaybackState.ACTION_PAUSE -> {
                            Timber.e("controlsCallback -  PlaybackState.ACTION_PAUSE")
                        }
                    }
                }
            }

        controller.registerCallback(controlsCallback)
        return controller
    }
}