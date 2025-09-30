package com.riders.thelab.feature.palette

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.riders.thelab.core.testing.utils.log
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PaletteActivityTest {

    @get:Rule(order = 0)
    val hiltInject: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityScenarioRule = ActivityScenarioRule(PaletteActivity::class.java)

    @Before
    fun setup() {
        println("========================= BEGINNING OF TEST =========================")

        hiltInject.inject()
    }

    @Test
    fun testImageLoading() {
        val scenario = activityScenarioRule.scenario
        scenario.onActivity {
            scenario.moveToState(Lifecycle.State.RESUMED)
            log(methodName = "testImageLoading", message = "Activity state moved to RESUMED")

            assert(true)
        }
    }
}