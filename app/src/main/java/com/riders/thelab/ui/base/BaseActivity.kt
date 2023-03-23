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
        for (declaredMethod in javaClass.declaredMethods) {
            if (declaredMethod.isAnnotationPresent(Listen::class.java)) {
                try {
                    declaredMethod.invoke(this)
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
}