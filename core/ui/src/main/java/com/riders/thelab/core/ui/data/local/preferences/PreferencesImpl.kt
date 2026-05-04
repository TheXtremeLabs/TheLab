package com.riders.thelab.core.ui.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.riders.thelab.core.ui.data.local.preferences.proto.DarkThemeConfigProto
import com.riders.thelab.core.ui.data.local.preferences.proto.ThemeColorConfigProto
import com.riders.thelab.core.ui.data.local.preferences.proto.UserPreferences
import com.riders.thelab.core.ui.data.local.preferences.serializer.UserPreferencesSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

val Context.theLabUiPreferencesDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "user_preferences.proto",
    serializer = UserPreferencesSerializer
)
@Singleton
class PreferencesImpl @Inject constructor(
    private val context: Context
) : IPreferences {

    // First Time Launched
    override suspend fun updateIsFirstTimeLaunched(isFirstTimeLaunched: Boolean) {
        context
            .theLabUiPreferencesDataStore
            .updateData { preferences ->
                preferences
                    .toBuilder()
                    .apply { setIsFirstTimeLaunched(isFirstTimeLaunched) }
                    .build()
            }
    }

    override fun getIsFirstTimeLaunched(): Flow<Boolean> = context
        .theLabUiPreferencesDataStore
        .data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                // 2 In the lambda block, you check if the exception is an instance of IOException.
                // If it is, you catch the exception and return an empty instance of Preferences.
                // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value of the original Flow.
            // In your case, you get the data by using a certain key, the PreferencesKeys.NIGHT_MODE_KEY.
            // If the key isn’t set when you try to read the data it returns null.
            // You use the Elvis operator to handle this and return false instead.
            preferences.isFirstTimeLaunched
        }


    // Theme Color
    override suspend fun updateThemeColor(themeColorConfig: ThemeColorConfigProto) {
        context.theLabUiPreferencesDataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .apply { setThemeConfig(themeColorConfig) }
                .build()
        }
    }

    override fun getThemeColor(): Flow<ThemeColorConfigProto> = context
        .theLabUiPreferencesDataStore
        .data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                // 2 In the lambda block, you check if the exception is an instance of IOException.
                // If it is, you catch the exception and return an empty instance of Preferences.
                // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value of the original Flow.
            // In your case, you get the data by using a certain key, the PreferencesKeys.NIGHT_MODE_KEY.
            // If the key isn’t set when you try to read the data it returns null.
            // You use the Elvis operator to handle this and return false instead.
            preferences.themeConfig
        }

    // Dark Theme
    override suspend fun updateThemeDarkConfig(darkThemeConfig: DarkThemeConfigProto) {
        Timber.d("updateThemeDarkConfig() | dark theme config: $darkThemeConfig")

        context.theLabUiPreferencesDataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .apply { setDarkThemeConfig(darkThemeConfig) }
                .build()
        }
    }

    override fun getThemeDarkConfig(): Flow<DarkThemeConfigProto> = context
        .theLabUiPreferencesDataStore
        .data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                // 2 In the lambda block, you check if the exception is an instance of IOException.
                // If it is, you catch the exception and return an empty instance of Preferences.
                // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value of the original Flow.
            // In your case, you get the data by using a certain key, the PreferencesKeys.NIGHT_MODE_KEY.
            // If the key isn’t set when you try to read the data it returns null.
            // You use the Elvis operator to handle this and return false instead.
            preferences.darkThemeConfig
        }

    override suspend fun updateThemeDarkConfigWithNumber(darkThemeConfigNumber: Int) {
        Timber.e("updateThemeDarkConfig() | darkThemeConfigNumber: $darkThemeConfigNumber")

        context.theLabUiPreferencesDataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .apply {
                    setDarkThemeConfig(DarkThemeConfigProto.forNumber(darkThemeConfigNumber))
                }
                .build()
        }
    }

    override fun getThemeDarkConfigInt(): Flow<Int> = context
        .theLabUiPreferencesDataStore
        .data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                // 2 In the lambda block, you check if the exception is an instance of IOException.
                // If it is, you catch the exception and return an empty instance of Preferences.
                // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value of the original Flow.
            // In your case, you get the data by using a certain key, the PreferencesKeys.NIGHT_MODE_KEY.
            // If the key isn’t set when you try to read the data it returns null.
            // You use the Elvis operator to handle this and return false instead.
            preferences.darkThemeConfig.number
        }

    // User Logged In
    override suspend fun updateIsUserLoggedIn(isUserLoggedIn: Boolean) {
        context
            .theLabUiPreferencesDataStore
            .updateData { preferences ->
                preferences
                    .toBuilder()
                    .apply { setIsUserLoggedIn(isUserLoggedIn) }
                    .build()
            }
     }

    override fun getIsUserLoggedIn(): Flow<Boolean> = context
        .theLabUiPreferencesDataStore
        .data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value
            preferences.isUserLoggedIn
        }


    // Activities Splash Screens
    override suspend fun updateIsActivitiesSplashEnabled(isActivitiesSplashEnabled: Boolean) {
        context
            .theLabUiPreferencesDataStore
            .updateData { preferences ->
                preferences
                    .toBuilder()
                    .apply { setIsActivitiesSplashEnabled(isActivitiesSplashEnabled) }
                    .build()
            }
    }

    override fun getIsActivitiesSplashEnabled(): Flow<Boolean> = context
        .theLabUiPreferencesDataStore
        .data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value of the original Flow.
            // In your case, you get the data by using a certain key, the PreferencesKeys.NIGHT_MODE_KEY.
            // If the key isn’t set when you try to read the data it returns null.
            // You use the Elvis operator to handle this and return false instead.
            preferences.isActivitiesSplashEnabled
        }

    // User onboarding
    override suspend fun updateShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        context
            .theLabUiPreferencesDataStore
            .updateData { preferences ->
                preferences
                    .toBuilder()
                    .apply { setShouldHideOnboarding(shouldHideOnboarding) }
                    .build()
            }
    }

    override fun getShouldHideOnboarding(): Flow<Boolean> = context
        .theLabUiPreferencesDataStore
        .data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                // 2 In the lambda block, you check if the exception is an instance of IOException.
                // If it is, you catch the exception and return an empty instance of Preferences.
                // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                emit(UserPreferences.getDefaultInstance())
            }
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value
            preferences.shouldHideOnboarding
        }

    // Haptic feedback
    override suspend fun updateIsVibrationEnabled(isVibrationEnabled: Boolean) {
        context
            .theLabUiPreferencesDataStore
            .updateData { preferences ->
                preferences
                    .toBuilder()
                    .apply { setIsVibrationEnabled(isVibrationEnabled) }
                    .build()
            }
    }

    override fun getIsVibrationEnabled(): Flow<Boolean> = context
        .theLabUiPreferencesDataStore
        .data
        .catch { exception ->
            // 1 On the first line, you access the data of DataStore.
            // This property returns a Flow.
            // Then you call catch() from the Flow API to handle any errors.
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                // 2 In the lambda block, you check if the exception is an instance of IOException.
                // If it is, you catch the exception and return an empty instance of Preferences.
                // If the exception isn’t IOException, you rethrow it or handle it in a way that works for you.
                emit(UserPreferences.getDefaultInstance())
            }
        }
        .map { preferences ->
            // 3 Finally, map() returns a Flow which contains the results of applying
            // the given function to each value
            preferences.isVibrationEnabled
        }

    override suspend fun deleteAllData() {
        Timber.e("deleteAllData()")

        updateThemeDarkConfigWithNumber(DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED.number)
        updateThemeColor(ThemeColorConfigProto.THEME_COLOR_DEFAULT)
    }
}