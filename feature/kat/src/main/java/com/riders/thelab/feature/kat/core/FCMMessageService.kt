package com.riders.thelab.feature.kat.core

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.remote.dto.kat.NotificationData
import com.riders.thelab.feature.kat.R
import com.riders.thelab.feature.kat.ui.KatMainActivity
import kotlinx.serialization.json.Json
import timber.log.Timber


class FCMMessageService : FirebaseMessagingService() {

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("onNewToken() | token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //applicationContext
        //sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.d("onMessageReceived() | from: ${remoteMessage.from}, message: ${remoteMessage.rawData.toString()}")
        Timber.d("should Show notification here")

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")

            // Check if data needs to be processed by long running job
            // if (needsToBeScheduled()) {
            // For long-running tasks (10 seconds or more) use WorkManager.
            // scheduleJob()
            //} else {
            // Handle message within 10 seconds
            //    handleNow()
            // }
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            showNotification(title!!, message!!)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.d("Message Notification Body: ${it.body}")
            it.body?.let { notificationBody ->
                val notification: NotificationData =
                    Json.decodeFromString<NotificationData>(notificationBody)
                showNotification(notification.title, notification.message)
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
        Timber.d("onMessageSent() | message ID: $msgId")
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Timber.e("onDeletedMessages")
    }


    // Method to display the notifications
    @SuppressLint("NewApi")
    fun showNotification(title: String, message: String) {
        // Pass the intent to switch to the MainActivity
        val intent = Intent(this, KatMainActivity::class.java)

        // Assign channel ID
        val channelID = "notification_channel"
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Pass the intent to PendingIntent to start the
        // next Activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channelID
        )
            .setSubText("Kat - new messages")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(com.riders.thelab.core.ui.R.drawable.ic_the_lab_12_logo_black)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(false)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        // builder = builder.setContent(getCustomDesign(title, message))
//        builder = builder.setCustomContentView(getBigCustomDesign(title, message))
//        builder = builder.setCustomBigContentView(getBigCustomDesign(title, message))

        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the Android Version is greater than Oreo
        if (LabCompatibilityManager.isOreo()) {
            val notificationChannel = NotificationChannel(
                channelID,
                applicationContext.getString(R.string.kat_notification_channel),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
        /*
                with(NotificationManagerCompat.from(this){

                }*/
    }

    // Method to get the custom Design for the display of
    // notification.
    private fun getCustomDesign(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews(
            applicationContext.packageName,
            R.layout.layout_kat_notification
        )

        remoteViews.setTextViewText(R.id.tv_title, title)
        remoteViews.setTextViewText(R.id.tv_message, message)
        remoteViews.setImageViewResource(
            R.id.iv_icon,
            com.riders.thelab.core.ui.R.drawable.ic_the_lab_12_logo_black
        )

        return remoteViews
    }

    private fun getBigCustomDesign(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews(
            applicationContext.packageName,
            R.layout.layout_kat_big_notification
        )

        remoteViews.setTextViewText(R.id.tv_title, title)
        remoteViews.setTextViewText(R.id.tv_message, message)
        remoteViews.setImageViewResource(
            R.id.iv_icon,
            com.riders.thelab.core.ui.R.drawable.ic_the_lab_12_logo_black
        )

        return remoteViews
    }
}