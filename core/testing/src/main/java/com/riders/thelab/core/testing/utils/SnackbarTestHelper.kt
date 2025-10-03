package com.riders.thelab.core.testing.utils

import android.view.View
import androidx.activity.ComponentActivity
import androidx.test.espresso.IdlingRegistry
import com.google.android.material.snackbar.Snackbar


/**
 * Utility to wait asynchronously for a Snackbar to appear and disappear.
 */
object SnackbarTestHelper {

    /**
     * Waits for a Snackbar to appear on [parentView], keeps test thread alive
     * until the Snackbar is dismissed. Works asynchronously and Espresso-compatible.
     */
    fun waitForSnackbar(activity: ComponentActivity, parentView: View, message: String) {
        val resource = DebugWaitEspresso.ManualIdlingResource("SnackbarWait")
        IdlingRegistry.getInstance().register(resource)

        println("======> waitForSnackbar() | Tell Espresso we're busy. Keeps activity alive indefinitely until manually released....")
        resource.busy()

        // Show listener to detect when Snackbar is dismissed
        /*UIManager.showSnackbar(
            activity = activity,
            rootView = parentView,
            message = message,
            duration = Snackbar.LENGTH_INDEFINITE,
            actionLabel = activity.getString(android.R.string.ok),
            callback = object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    super.onShown(sb)
                    println("======> waitForSnackbar() | onShown")
                    try {
                        resource.idle()
                        IdlingRegistry.getInstance().unregister(resource)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    println("======> waitForSnackbar() | onDismissed")
                    try {
                        resource.idle()
                        IdlingRegistry.getInstance().unregister(resource)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },
            action = {
                println("======> waitForSnackbar() | snackBar action clicked")
                it.dismiss()
            }
        )*/
    }
}