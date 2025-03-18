package com.riders.thelab.core.ui.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.data.valueOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

val Context.theLabUiThemePreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "DATASTORE_APP_THEME_FILE_NAME"
)

class PreferencesImpl @Inject constructor(
    private val context: Context
) : IPreferences {

    override suspend fun updateThemeDarkMode(isDarkMode: Boolean) {
        context.theLabUiThemePreferencesDataStore.edit {
            it[PreferencesKeys.DATASTORE_DARK_MODE_KEY] = isDarkMode
        }
    }

    override fun isThemeDarkMode(): Flow<Boolean> =
        context.theLabUiThemePreferencesDataStore.data.catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                // 2 In the lambda block, you check if the exception is an instance of IOException.
                // If it is, you catch the exception and return an empty instance of Preferences.
                // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value of the original Flow.
            // In your case, you get the data by using a certain key, the PreferencesKeys.NIGHT_MODE_KEY.
            // If the key isn’t set when you try to read the data it returns null.
            // You use the Elvis operator to handle this and return false instead.
            it[PreferencesKeys.DATASTORE_DARK_MODE_KEY] ?: false
        }

    override suspend fun updateTheme(appTheme: AppTheme) {
        context.theLabUiThemePreferencesDataStore.edit {
            it[PreferencesKeys.DATASTORE_APP_THEME_NAME_KEY] = appTheme.name
        }
    }

    override fun getTheme(): Flow<AppTheme> =
        context.theLabUiThemePreferencesDataStore.data.catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                // 2 In the lambda block, you check if the exception is an instance of IOException.
                // If it is, you catch the exception and return an empty instance of Preferences.
                // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value of the original Flow.
            // In your case, you get the data by using a certain key, the PreferencesKeys.NIGHT_MODE_KEY.
            // If the key isn’t set when you try to read the data it returns null.
            // You use the Elvis operator to handle this and return false instead.
            it[PreferencesKeys.DATASTORE_APP_THEME_NAME_KEY]?.let { appThemeName ->
                valueOf<AppTheme>(appThemeName)
            } ?: AppTheme.Default
        }

    override suspend fun deleteAllData() {
        Timber.e("deleteAllData()")

        updateThemeDarkMode(false)
        updateTheme(AppTheme.Default)
    }
}