package com.riders.thelab.core.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.LabNotificationManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.ui.splashscreen.SplashScreenActivity
import com.riders.thelab.utils.Constants
import timber.log.Timber

class TheLabBootService : Service() {

    //binder given to client
    private val mBinder: IBinder = LocalBinder()

    // Create an explicit intent for an Activity in your app
    private lateinit var mIntent: Intent
    private lateinit var mPendingIntent: PendingIntent


    override fun onBind(intent: Intent?): IBinder {
        Timber.i("KTestingBootService - onBind()")
        UIManager.showActionInToast(
            this@TheLabBootService,
            "KTestingBootService - onBind()"
        )
        return mBinder
    }

    @SuppressLint("InlinedApi")
    override fun onCreate() {
        super.onCreate()

        if (LabCompatibilityManager.isNougat()) {
            LabNotificationManager.createNotificationChannel(
                this,
                getString(R.string.main_channel_name),
                getString(R.string.main_channel_description),
                NotificationManager.IMPORTANCE_DEFAULT,
                Constants.NOTIFICATION_CHANNEL_ID
            )
        }
        displaySessionNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun displaySessionNotification() {
        if (!LabCompatibilityManager.isOreo()) {
            UIManager.showActionInToast(this@TheLabBootService, "onReceive notification builder()")
            Timber.e("onReceive notification builder()")

            val notificationIntent =
                Intent(this@TheLabBootService, SplashScreenActivity::class.java)
            notificationIntent.addFlags(
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_SINGLE_TOP
            )

            val contentIntent: PendingIntent = PendingIntent.getActivity(
                this@TheLabBootService,
                0,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            val mBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this).apply {
                    setSmallIcon(R.mipmap.ic_lab_six_round)
                    setContentTitle(getString(R.string.notification_title))
                    setContentText(getString(R.string.notification_content_text))
                    setTicker(getString(R.string.notification_title))
                    setAutoCancel(false)
                    addAction(
                        R.drawable.ic_close,
                        "Cancel notification", contentIntent
                    )
                    setContentIntent(contentIntent)
                }

            val mNotifyMgr: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            mNotifyMgr.notify(Constants.NOTIFICATION_ID, mBuilder.build())

        } else {
            mIntent =
                Intent(this@TheLabBootService, SplashScreenActivity::class.java)
                    .apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
            mPendingIntent =
                PendingIntent.getActivity(this@TheLabBootService, 0, mIntent, 0)

            val notification: NotificationCompat.Builder =
                LabNotificationManager.buildMainNotification(this, mPendingIntent)

            // notificationId is a unique int for each notification that you must define
            startForeground(Constants.NOTIFICATION_ID, notification.build())
        }
    }

    //our inner class
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): TheLabBootService {
            return this@TheLabBootService
        }
    }
}
