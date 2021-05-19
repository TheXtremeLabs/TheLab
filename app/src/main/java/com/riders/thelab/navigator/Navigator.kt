package com.riders.thelab.navigator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.riders.thelab.ui.contacts.addcontact.AddContactActivity
import com.riders.thelab.ui.mainactivity.MainActivity
import com.riders.thelab.ui.splashscreen.SplashScreenActivity
import timber.log.Timber
import javax.inject.Inject

class Navigator @Inject constructor(
    context: Activity
) {
    private val activity: Activity = context


    /* Activities */
    fun callSplashActivity() {
        activity.startActivity(Intent(activity, SplashScreenActivity::class.java))
    }

    fun callMainActivity() {
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }

    /* Detail Activities */
    fun callContactDetailActivity(intent: Intent?) {
        activity.startActivity(intent)
    }

    fun callAddContactActivity() {
        activity.startActivity(Intent(activity, AddContactActivity::class.java))
    }

    fun callMultipaneDetailActivity(intent: Intent?) {
        activity.startActivity(intent)
    }

    fun callYoutubeDetailActivity(intent: Intent, optionsCompat: Bundle?) {
        Timber.d("Apply activity transition")
        activity.startActivity(intent, optionsCompat)
    }

    /* Activities */
    fun callIntentActivity(targetClass: Class<out Activity?>?) {
        activity.startActivity(Intent(activity, targetClass))
    }

    /* Packages */
    fun callIntentForPackageActivity(intentPackageName: String?) {
        activity.startActivity(
            intentPackageName?.let {
                activity
                    .packageManager
                    .getLaunchIntentForPackage(it)
            })
    }
}