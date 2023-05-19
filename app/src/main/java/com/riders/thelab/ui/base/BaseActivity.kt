package com.riders.thelab.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.riders.thelab.core.bus.Listen
import timber.log.Timber
import java.lang.reflect.InvocationTargetException

open class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate()")
        subscribeToKotlinBus()
    }

    private fun subscribeToKotlinBus() {
        Timber.i("subscribeToKotlinBus()")
        javaClass.declaredMethods
            .filter { it.isAnnotationPresent(Listen::class.java) }
            .forEach {
                try {
                    it.invoke(this)
                } catch (e: IllegalAccessException) {
                    // Utils.logException(e)
                    Timber.e(e)
                } catch (e: InvocationTargetException) {
                    // Utils.logException(e)
                    Timber.e(e)
                }
            }
    }
}