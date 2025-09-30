package com.riders.thelab.core.testing

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TheLabHiltTestRunner: AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application? {

        // This tells Hilt to use HiltTestApplication as the base,
        // which then knows how to work with your @HiltAndroidApp CustomTheLabHiltTest
        return super.newApplication(cl, CustomTheLabHiltTest_Application::class.java.name, context)
    }
}