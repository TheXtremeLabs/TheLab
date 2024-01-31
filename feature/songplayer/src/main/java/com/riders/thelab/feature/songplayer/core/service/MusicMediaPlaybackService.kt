package com.riders.thelab.feature.songplayer.core.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.session.MediaButtonReceiver
import timber.log.Timber

class MusicMediaPlaybackService : Service() {

    //binder given to client
    private val mBinder: IBinder = LocalBinder()

    private var mMediaSessionCompat: MediaSessionCompat? = null

    override fun onBind(intent: Intent?): IBinder {
        Timber.d("onBind()")
        mMediaSessionCompat = MediaSessionCompat(this, "MediaPlaybackService")
        return mBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand()")
        // TODO : Fix notification media player buttons event
        if (null != mMediaSessionCompat) {
            MediaButtonReceiver.handleIntent(mMediaSessionCompat, intent)
        }
        return super.onStartCommand(intent, flags, startId)
    }


    //our inner class
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MusicMediaPlaybackService {
            return this@MusicMediaPlaybackService
        }
    }
}