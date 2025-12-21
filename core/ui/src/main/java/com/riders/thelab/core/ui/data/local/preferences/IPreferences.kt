package com.riders.thelab.core.ui.data.local.preferences

import com.riders.thelab.core.ui.data.local.preferences.proto.DarkThemeConfigProto
import com.riders.thelab.core.ui.data.local.preferences.proto.ThemeColorConfigProto
import kotlinx.coroutines.flow.Flow

interface IPreferences {

    // First time launched
    suspend fun updateIsFirstTimeLaunched(isFirstTimeLaunched: Boolean)
    fun getIsFirstTimeLaunched(): Flow<Boolean>

    // Theme Color
    suspend fun updateThemeColor(themeColorConfig: ThemeColorConfigProto)
    fun getThemeColor(): Flow<ThemeColorConfigProto>

    // Dark Theme
    suspend fun updateThemeDarkConfig(darkThemeConfig: DarkThemeConfigProto)
    fun getThemeDarkConfig(): Flow<DarkThemeConfigProto>
    suspend fun updateThemeDarkConfigWithNumber(darkThemeConfigNumber: Int)
    fun getThemeDarkConfigInt(): Flow<Int>

    // User Logged In
    suspend fun updateIsUserLoggedIn(isUserLoggedIn: Boolean)
    fun getIsUserLoggedIn(): Flow<Boolean>

    // Activities Splash Screens
    suspend fun updateIsActivitiesSplashEnabled(isActivitiesSplashEnabled: Boolean)
    fun getIsActivitiesSplashEnabled(): Flow<Boolean>

    // User onboarding
    suspend fun updateShouldHideOnboarding(shouldHideOnboarding: Boolean)
    fun getShouldHideOnboarding(): Flow<Boolean>

    // Haptic feedback
    suspend fun updateIsVibrationEnabled(isVibrationEnabled: Boolean)
    fun getIsVibrationEnabled(): Flow<Boolean>

    suspend fun deleteAllData()
}