package com.riders.thelab.core.common.utils

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.riders.thelab.core.common.utils.LabAppManager.getAppListFromAssets
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class LabAppManagerTest {
    private lateinit var instrumentationContext: Context
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()


    @Before
    fun setup() {
        println("==================BEGINNING OF TEST==================")
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testGetAppListFromAssets() {
        println("================== testGetAppListFromAssets() ==================")

        val appList: List<Any>? = instrumentationContext.getAppListFromAssets<Any>()
        appList.also { println("=====> testGetAppListFromAssets() | result $it") }
        assert(!appList.isNullOrEmpty())
    }


    @After
    fun tearDown() {
        println("==================ENDING OF TEST==================")
    }
}