package com.riders.thelab.core.testing.utils

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import java.util.concurrent.CountDownLatch

val latch = java.util.concurrent.CountDownLatch(1)

val resource = object : IdlingResource {
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName() = "ManualWait"
    override fun isIdleNow() = latch.count == 0L
    override fun registerIdleTransitionCallback(cb: IdlingResource.ResourceCallback?) {
        callback = cb
    }

    fun release() {
        latch.countDown()
        callback?.onTransitionToIdle()
    }
}

fun waitFor(delayMillis: Long, resourceName: String = "Timer") {
    val resource = CountingIdlingResource(resourceName)
    IdlingRegistry.getInstance().register(resource)
    resource.increment()
    Handler(Looper.getMainLooper()).postDelayed({
        resource.decrement()
        IdlingRegistry.getInstance().unregister(resource)
    }, delayMillis)
}

/**
 * Utility to keep an Activity open during instrumentation tests
 * for visual debugging.
 *
 * ⚠️ Only for debug purposes. Do NOT use in CI tests.
 */
object DebugWait {

    private var manualLatch: CountDownLatch? = null

    /**
     * Keep the activity open for a specific amount of time (ms)
     * without blocking the UI thread.
     */
    fun keepActivityOpenFor(delayMillis: Long) {
        val latch = CountDownLatch(1)
        Handler(Looper.getMainLooper()).postDelayed({
            latch.countDown()
        }, delayMillis)

        // Blocks test thread, UI thread remains free
        latch.await()
    }

    /**
     * Keep the activity open indefinitely until manually released
     * (e.g., stop test in Android Studio).
     */
    fun keepActivityOpenIndefinitely() {
        manualLatch = CountDownLatch(1)
        manualLatch?.await()
    }

    /**
     * Release the manually kept activity, ends the wait
     */
    fun releaseManualWait() {
        manualLatch?.countDown()
        manualLatch = null
    }
}

/**
 * Debug helper for keeping Activities open asynchronously
 * while being compatible with Espresso UI tests.
 *
 * ⚠️ Only for debug purposes. Not for CI tests.
 */
object DebugWaitEspresso {

    private var manualLatch: CountDownLatch? = null
    private var manualResource: ManualIdlingResource? = null

    /**
     * Keep the activity open for a specific duration (ms)
     * UI thread stays responsive; Espresso waits automatically.
     */
    fun keepActivityOpenFor(delayMillis: Long) {
        val resource = ManualIdlingResource("DebugWaitTimer")
        IdlingRegistry.getInstance().register(resource)
        resource.busy()

        Handler(Looper.getMainLooper()).postDelayed({
            resource.idle()
            IdlingRegistry.getInstance().unregister(resource)
        }, delayMillis)
    }

    /**
     * Keep the activity open indefinitely until manually released.
     */
    fun keepActivityOpenIndefinitely() {
        manualLatch = CountDownLatch(1)
        manualResource = ManualIdlingResource("DebugWaitManual")
        IdlingRegistry.getInstance().register(manualResource!!)
        manualResource!!.busy()

        // Blocks test thread asynchronously
        manualLatch!!.await()
    }

    /**
     * Release the manual wait
     */
    fun releaseManualWait() {
        manualLatch?.countDown()
        manualResource?.idle()
        manualLatch = null
        manualResource = null
    }

    /**
     * Internal Espresso IdlingResource implementation
     */
    class ManualIdlingResource(private val name: String) : IdlingResource {
        @Volatile private var callback: IdlingResource.ResourceCallback? = null
        @Volatile private var idle = true

        override fun getName() = name
        override fun isIdleNow() = idle
        override fun registerIdleTransitionCallback(cb: IdlingResource.ResourceCallback?) {
            callback = cb
        }

        fun busy() {
            idle = false
        }

        fun idle() {
            idle = true
            callback?.onTransitionToIdle()
        }
    }
}