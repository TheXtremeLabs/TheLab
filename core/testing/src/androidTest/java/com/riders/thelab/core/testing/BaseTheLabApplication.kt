package com.riders.thelab.core.testing

import androidx.multidex.MultiDexApplication
import com.riders.thelab.core.testing.base.BaseTheLabApplication
import com.riders.thelab.core.testing.utils.log
import dagger.hilt.android.testing.CustomTestApplication

abstract class BaseTheLabApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        log(
            methodName = "onCreate",
            message = "${BaseTheLabApplication::class.java.simpleName} successfully initialized"
        )
    }
}

@CustomTestApplication(BaseTheLabApplication::class)
interface CustomTheLabHiltTest