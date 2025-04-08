package com.riders.thelab.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

/**
 * Reads data from a DataStore, safely handling potential corruption and I/O errors.
 *
 * This function wraps DataStore's data access with error handling to prevent application crashes
 * due to data corruption or I/O issues. It provides multiple layers of fallback mechanisms
 * to ensure a value is always emitted.
 *
 * @param getData A lambda function that takes the `Preferences` object and extracts the desired
 *                data (type `T`) from it. This is where you define how to retrieve the specific
 *                value you need from the preferences.
 * @param defaultValue The default value of type `T` to be emitted if there are errors reading
 *                     or parsing the preferences.
 * @return A `Flow` emitting the extracted data of type `T`. The `Flow` will emit the
 *         `defaultValue` in case of unrecoverable errors or corruption, or an empty preferences when corruption is detected.
 *
 * **Error Handling Strategy:**
 *
 * 1. **Corruption Handling (`CorruptionException`):**
 *    - If a `CorruptionException` occurs while reading the DataStore, it indicates that the
 *      stored preferences are corrupt and cannot be parsed.
 *    - In this case, the function logs the error using Timber and attempts to reset the DataStore
 *      by emitting an `emptyPreferences()`. This effectively clears all existing data in the store.
 *    - The next step after clearing the preference will execute a flow with `emptyPreferences()`
 *
 * 2. **I/O Error Handling (`IOException`):**
 *    - If an `IOException` occurs, it means there was an issue reading the DataStore file (e.g.,
 *      file not found, permissions issue).
 *    - The function logs the error and emits the existing preferences. This attempts to preserve
 *      any data that could be read.
 *    - `updateData` is called with the function that returns the existing preferences so that the stored data is preserved.
 *
 * 3. **`updateData` Failure Fallback:**
 *    - If the `updateData` operation (used for clearing corrupt data or preserving existing preferences)
 *      also fails (e.g., due to another exception), another `catch` block handles this.
 */
fun <T> DataStore<Preferences>.safeRead(
    getData: (preferences: Preferences) -> T,
    defaultValue: T
): Flow<T> {
    return this.data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            when (exception) {
                is CorruptionException -> {
                    Timber.e(exception, "safeRead() | Corruption in preferences, recreating store")

                    // Send exception to firebase in order to get the stacktrace
                    FirebaseCrashlytics.getInstance().recordException(exception)

                    // If corruption is detected, clear all data
                    emit(this@safeRead.updateData { emptyPreferences() })
                }

                is IOException -> {
                    // 2 In the lambda block, you check if the exception is an instance of IOException.
                    // If it is, you catch the exception and return an empty instance of Preferences.
                    // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                    Timber.e(exception, "safeRead() | Error reading preferences")

                    // Send exception to firebase in order to get the stacktrace
                    FirebaseCrashlytics.getInstance().recordException(exception)

                    emit(this@safeRead.updateData { preferences ->
                        // Return existing preferences
                        preferences
                    })
                }

                else -> {
                    // Send exception to firebase in order to get the stacktrace
                    FirebaseCrashlytics.getInstance().recordException(exception)

                    throw exception
                }
            }
        }
        .catch { exception ->
            // Fallback in case the updateData above also fails
            Timber.e(exception, "safeRead() | Fallback: returning default value")

            // Send exception to firebase in order to get the stacktrace
            FirebaseCrashlytics.getInstance().recordException(exception)

            emit(this@safeRead.updateData { emptyPreferences() })
        }
        .catch {
            // Send exception to firebase in order to get the stacktrace
            FirebaseCrashlytics.getInstance().recordException(it)

            // Final fallback - return default value
            emit(emptyPreferences())
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value of the original Flow.
            // In your case, you get the data by using a certain key, the PreferencesKeys.NIGHT_MODE_KEY.
            // If the key isn’t set when you try to read the data it returns null.
            try {
                getData(preferences)
            } catch (e: Exception) {
                Timber.e(e, "safeRead() | Error mapping preferences")
                defaultValue
            }
        }
}

/**
 * Safely performs a write operation on a DataStore of Preferences, handling potential errors.
 *
 * This function wraps the DataStore's `edit` operation within a try-catch block to gracefully handle
 * exceptions that may occur during the write process. It specifically addresses `CorruptionException`,
 * which can happen if the underlying data file is corrupted.
 *
 * **Error Handling:**
 * - If any exception occurs during the `edit` operation, it will be caught.
 * - A log entry with the error details will be made using Timber.
 * - If the caught exception is a `CorruptionException`, the following recovery process will be attempted:
 *   1. The DataStore's content will be cleared using `preferences.clear()`.
 *   2. The original `update` lambda will be re-executed to re-populate the DataStore.
 *
 * **Usage:**
 *
 * This function is designed to replace direct calls to `DataStore.edit` when you need robust error handling.
 * It ensures that your application doesn't crash due to unexpected issues during preference updates.
 *
 * ```kotlin
 * // Example Usage:
 * lifecycleScope.launch {
 *     myPreferencesDataStore.safeEdit { preferences ->
 *         preferences[MY_PREFERENCE_KEY] = "new value"
 *     }
 * }
 * ```
 *
 * @param update A suspend lambda function that receives a `MutablePreferences` object, allowing
 *               you to modify the preferences within the DataStore. This lambda should define
 *               the desired changes to the preferences.
 */
suspend fun DataStore<Preferences>.safeEdit(update: suspend (MutablePreferences) -> Unit) {
    try {
        this.edit { preferences ->
            update(preferences)
        }
    } catch (e: Exception) {
        Timber.e(e, "safeEdit() | Error writing to preferences")
        // Attempt to recreate store if corrupt
        if (e is CorruptionException) {

            // Send exception to firebase in order to get the stacktrace
            FirebaseCrashlytics.getInstance().recordException(e)

            this.edit {
                // Clear and retry the update
                    preferences ->
                preferences.clear()
                update(preferences)
            }
        }
    }
}