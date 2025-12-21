package com.riders.thelab.core.ui.data.local

import androidx.compose.runtime.Composable
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.data.local.preferences.IPreferences
import com.riders.thelab.core.ui.data.local.preferences.proto.DarkThemeConfigProto
import com.riders.thelab.core.ui.data.local.preferences.proto.ThemeColorConfigProto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class UiRepository @Inject constructor(preferences: IPreferences) : IUiRepository {

    private val mPreferencesImpl: IPreferences = preferences

    // Implement methods here for data operations
    // First Time Launched
    override suspend fun updateIsFirstTimeLaunched(isFirstTimeLaunched: Boolean) =
        mPreferencesImpl.updateIsFirstTimeLaunched(isFirstTimeLaunched)

    override fun getIsFirstTimeLaunched(): Flow<Boolean> = mPreferencesImpl
        .getIsFirstTimeLaunched()
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()

    // Theme Color
    override suspend fun updateThemeColor(themeColorConfig: ThemeColorConfigProto) =
        mPreferencesImpl.updateThemeColor(themeColorConfig)

    override fun getThemeColor(): Flow<ThemeColorConfigProto> = mPreferencesImpl
        .getThemeColor()
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()

    // Dark Theme
    override suspend fun updateThemeDarkConfig(darkThemeConfig: DarkThemeConfigProto) =
        mPreferencesImpl.updateThemeDarkConfig(darkThemeConfig)

    override fun getThemeDarkConfig(): Flow<DarkThemeConfigProto> = mPreferencesImpl
        .getThemeDarkConfig()
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()

    override suspend fun updateThemeDarkConfigWithNumber(darkThemeConfigNumber: Int) =
        mPreferencesImpl.updateThemeDarkConfigWithNumber(darkThemeConfigNumber)

    override fun getThemeDarkConfigInt(): Flow<Int> =
        mPreferencesImpl.getThemeDarkConfigInt()

    // User Logged In
    override suspend fun updateIsUserLoggedIn(isUserLoggedIn: Boolean) =
        mPreferencesImpl.updateIsUserLoggedIn(isUserLoggedIn)

    override fun getIsUserLoggedIn(): Flow<Boolean> = mPreferencesImpl.getIsUserLoggedIn()

    // Activities Splash Screens
    override suspend fun updateIsActivitiesSplashEnabled(isActivitiesSplashEnabled: Boolean) =
        mPreferencesImpl.updateIsActivitiesSplashEnabled(isActivitiesSplashEnabled)

    override fun getIsActivitiesSplashEnabled(): Flow<Boolean> = mPreferencesImpl
        .getIsActivitiesSplashEnabled()
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()

    // User onboarding
    override suspend fun updateShouldHideOnboarding(shouldHideOnboarding: Boolean) =
        mPreferencesImpl.updateShouldHideOnboarding(shouldHideOnboarding)

    override fun getShouldHideOnboarding(): Flow<Boolean> = mPreferencesImpl
        .getShouldHideOnboarding()
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()

    // Haptic feedback
    override suspend fun updateIsVibrationEnabled(isVibrationEnabled: Boolean) =
        mPreferencesImpl.updateIsVibrationEnabled(isVibrationEnabled)

    override fun getIsVibrationEnabled(): Flow<Boolean> = mPreferencesImpl
        .getIsVibrationEnabled()
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()


    override suspend fun deleteAllData() = mPreferencesImpl.deleteAllData()


    override fun getThemeDarkConfigSync(): DarkThemeConfigProto =
        runBlocking(Dispatchers.IO) { getThemeDarkConfig().first() }

    override fun getThemeColorSync(): ThemeColorConfigProto =
        runBlocking(Dispatchers.IO) { getThemeColor().first() }

    override fun getThemeColorAsAppTheme(): Flow<AppTheme> =
        combine(getThemeColor()) { themeColor ->
            Timber.e("getThemeColorAsAppTheme() | themeColor: ${themeColor.first()}")
            return@combine when (themeColor.first()) {
                ThemeColorConfigProto.THEME_COLOR_DEFAULT -> AppTheme.Default
                ThemeColorConfigProto.THEME_COLOR_BLUE -> AppTheme.Blue
                ThemeColorConfigProto.THEME_COLOR_GREEN -> AppTheme.Green
                ThemeColorConfigProto.THEME_COLOR_RED -> AppTheme.Red
                else -> AppTheme.Default
            }
        }

    override fun getThemeColorAsAppThemeSync(): AppTheme =
        runBlocking(Dispatchers.IO) { getThemeColorAsAppTheme().first() }

    override fun isDarkTheme(): Flow<Boolean?> = getThemeDarkConfig()
        .map { darkThemeConfig ->
            Timber.e("isDarkTheme() | darkThemeConfig: $darkThemeConfig")
            return@map when (darkThemeConfig) {
                DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> false
                DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> true
                else -> null
            }
        }

    @Composable
    override fun isDarkThemeComposable(
        isSystemInDark: Boolean
    ): Flow<Boolean> = getThemeDarkConfig().map { darkThemeConfig ->
        Timber.e("isDarkTheme(isSystemInDark = $isSystemInDark) | darkThemeConfig: $darkThemeConfig")
        return@map when (darkThemeConfig) {
            DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> false
            DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> true
            DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
            DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED -> isSystemInDark

            else -> isSystemInDark
        }
    }
}