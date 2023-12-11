package com.riders.thelab.feature.kat.core

import com.google.firebase.messaging.FirebaseMessagingService
import timber.log.Timber

class FCMMessageService: FirebaseMessagingService (){

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
}