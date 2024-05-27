package com.riders.thelab.core.player.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ConnectionResult
import androidx.media3.session.MediaSession.ConnectionResult.AcceptedResultBuilder
import androidx.media3.session.MediaSessionService
import timber.log.Timber

class PlaybackService : MediaSessionService() {
    private var mMediaSession: MediaSession? = null

    //binder given to client
    private val mBinder: IBinder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {
        super.onBind(intent)
        Timber.d("onBind()")
        /*if (null != mPlayer) {
            mMediaSession = MediaSession.Builder(this, mPlayer!!).build()
        }*/
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand()")
        // TODO : Fix notification media player buttons event
        if (null != mMediaSession) {
            // MediaButtonReceiver.handleIntent(mMediaSession, intent)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // Create your player and media session in the onCreate lifecycle event
    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate()")
        val player = ExoPlayer.Builder(this).build()
        mMediaSession = MediaSession.Builder(this, player).setCallback(MyCallback()).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mMediaSession

    override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
        super.onUpdateNotification(session, startInForegroundRequired)
        Timber.d("onUpdateNotification() | startInForegroundRequired: $startInForegroundRequired")
    }


    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        Timber.w("onTaskRemoved()")
        val player = mMediaSession?.player!!
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            Timber.e("call stopSelf()")
            stopSelf()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Timber.e("onLowMemory()")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Timber.i("onTrimMemory()")
    }

    fun getMediaSssion(): MediaSession? = mMediaSession

    fun playSong(mediaItem: MediaItem) {
        Timber.d("playSong(mediaItem)")

        mMediaSession?.player?.let { player ->
            if (player.isPlaying) {
                stop()
            }

            player.setMediaItem(mediaItem)
            player.prepare()
            // Play song
            player.play()
        }
    }

    fun isPlaying(): Boolean = mMediaSession?.player?.isPlaying ?: false

    fun playPause() {
        mMediaSession?.player?.let {
            if (!it.isPlaying) {
                it.play()
            } else {
                it.pause()
            }
        }
    }

    private fun stop() {
        mMediaSession?.player?.stop()
    }


    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        Timber.e("onDestroy()")
        mMediaSession?.run {
            player.release()
            release()
            mMediaSession = null
        }
        super.onDestroy()
    }

    //our inner class
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): PlaybackService {
            return this@PlaybackService
        }
    }

    private inner class MyCallback : MediaSession.Callback {
        @OptIn(UnstableApi::class)
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): ConnectionResult {
            // Set available player and session commands.
            return AcceptedResultBuilder(session)
                /*.setAvailablePlayerCommands(
                    ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
                        .remove(Player.COMMAND_SEEK_TO_NEXT)
                        .remove(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                        .remove(Player.COMMAND_SEEK_TO_PREVIOUS)
                        .remove(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
                        .build()
                )
                .setAvailableSessionCommands(
                    ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                        //.add(customCommandFavorites)
                        .build()
                )*/
                .build()
        }
    }
}