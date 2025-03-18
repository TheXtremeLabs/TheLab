package com.riders.thelab.core.ui.data.local.preferences

import com.riders.thelab.core.ui.compose.data.AppTheme
import kotlinx.coroutines.flow.Flow

interface IPreferences {

    suspend fun updateThemeDarkMode(isDarkMode: Boolean)
    fun isThemeDarkMode(): Flow<Boolean>
    suspend fun updateTheme(appTheme: AppTheme)
    fun getTheme(): Flow<AppTheme>

    suspend fun deleteAllData()
}