package com.riders.thelab.core.location

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.Executor
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SmallTest
@RunWith(AndroidJUnit4::class)
class LabLocationWorkerTest {

    private lateinit var context: Context
    private lateinit var executor: Executor

    val standardDispatcher = StandardTestDispatcher()

    val permissions = if (LabCompatibilityManager.isPie()) arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    ) else arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    @get:Rule(order = 0)
    val permissionRule: GrantPermissionRule get() = GrantPermissionRule.grant(*permissions)

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    @Throws(Exception::class)
    fun test_get_location_sync() = runTest(standardDispatcher) {
        // Define input data
        val input = workDataOf("KEY_1" to 1, "KEY_2" to 2)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .build()

        // Create request
        val request = OneTimeWorkRequestBuilder<LabLocationWorker>()
            .addTag(LabLocationWorker.TAG)
            .setConstraints(constraints)
            .setInputData(input)
            .build()

        val workManager = WorkManager.getInstance(context)
        val testDriver = WorkManagerTestInitHelper.getTestDriver()

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        workManager.enqueue(request).result.get()

        // Tells the testing framework that all constraints are met.
        testDriver?.setAllConstraintsMet(request.id)

        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get().also {
            println("=====> test_get_location() | worker info : ${it.toString()}")
        }
        val outputData = workInfo?.outputData.also {
            println("=====> test_get_location() | output data : ${it.toString()}")
        }

        // Assert
        //assertTrue (workInfo?.state == WorkInfo.State.SUCCEEDED)
        assertFalse(outputData == input)
    }

    @Test
    @Throws(Exception::class)
    fun test_get_location_async() {
        val workerTestTag = "${LabLocationWorker.TAG}_TEST"
        val workerTestUUID = UUID.randomUUID()

        // Define input data
        val input = workDataOf("KEY_1" to 1, "KEY_2" to 2)

        // Create request
        val worker = TestListenableWorkerBuilder<LabLocationWorker>(
            context = context,
            inputData = input
        ).apply {
            this.setId(workerTestUUID)
            this.setTags(listOf(workerTestTag))
        }
            .build()

        runBlocking {
            val result = worker.doWork().also { listenableWorkerResult ->
                println("=====> test_get_location_async() | result : ${listenableWorkerResult.toString()}")
            }

            // Assert
            assertTrue(result is ListenableWorker.Result.Success)

            val outputData = result.outputData
            assertNotNull(outputData)

            val outputDataLatitude =
                result.outputData.getDouble(LabLocationWorker.EXTRA_LOCATION_LATITUDE, 0.0).also {
                    println("=====> test_get_location() | output data - latitude : $it")
                }
            assertTrue(0.0 != outputDataLatitude)

            val outputDataLongitude =
                result.outputData.getDouble(LabLocationWorker.EXTRA_LOCATION_LONGITUDE, 0.0).also {
                    println("=====> test_get_location() | output data - longitude : $it")
                }
            assertTrue(0.0 != outputDataLongitude)
        }
    }
}