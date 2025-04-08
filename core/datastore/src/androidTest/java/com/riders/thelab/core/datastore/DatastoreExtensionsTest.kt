package com.riders.thelab.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "DATASTORE_FILENAME")

@SmallTest
@RunWith(AndroidJUnit4::class)
class DatastoreExtensionsTest {

    private lateinit var instrumentationContext: Context
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var datastorePreferences: DataStore<Preferences>


    @Before
    fun setup() {
        println("==================BEGINNING OF TEST==================")

        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext.apply {
            datastorePreferences = this.dataStore
        }

    }

    // Add tests for PreferencesManager
    @Test
    fun testSaveAndDisplayLaunchTime() = runTest(testDispatcher) {
        // Given
        updateIdc11(POINT_ID_C11)

        // When
        val launchTime = datastorePreferences
            .safeRead({ preferences -> preferences[KEY_LAUNCH_TIME] }, 0)
            .first()
            .also {
            println("===> testSaveAndDisplayLaunchTime() | launchTime: $it")
        }

        // Then
        launchTime?.let { assert(0 < launchTime) } ?: run { assert(false) }
    }


    @Test
    fun testSaveLaunchTimeTwiceAndDisplay() = runTest(testDispatcher) {

        // Given
        for (i in 1..2) { updateIdc11(POINT_ID_C11) }

        // When
        val launchTime = datastorePreferences
            .safeRead({ preferences -> preferences[KEY_LAUNCH_TIME] }, 0)
            .first()
            .also {
            println("===> testSaveLaunchTimeTwiceAndDisplay() | launchTime: $it")
        }

        // Then
        launchTime?.let { assert(2 == it) } ?: run { assert(false) }
    }

    @After
    fun tearDown() {
        runBlocking {
            datastorePreferences.safeEdit { preferences ->
                println("===> tearDown() | clear preferences")
                preferences.clear()
            }
        }
        println("==================ENDING OF TEST==================")
    }

    private suspend fun updateIdc11(newIdc11: String) {
        println(" ====> updateIdc11() | newIdc11: $newIdc11")

        // With datastore
        datastorePreferences.safeEdit { preferences ->
            val currentIdc11: String = preferences[KEY_LAST_IDC11] ?: ""
            val currentLaunchTime = preferences[KEY_LAUNCH_TIME] ?: 0

            if (currentIdc11 == newIdc11) {
                preferences[KEY_LAUNCH_TIME] = currentLaunchTime + 1
            } else {
                preferences[KEY_LAST_IDC11] = newIdc11
                preferences[KEY_LAUNCH_TIME] = 1
            }
        }
    }

    companion object {
        private const val POINT_ID_C11 = "NEY_494958_XV23474759475FR"

        // For SharedPreferences
        private val SP_KEY_LAST_IDC11: String = "last_idc11"
        private val SP_KEY_LAUNCH_TIME: String = "launch_time"

        // For Datastore
        private val KEY_LAST_IDC11 = stringPreferencesKey("last_idc11")
        private val KEY_LAUNCH_TIME = intPreferencesKey("launch_time")
    }
}