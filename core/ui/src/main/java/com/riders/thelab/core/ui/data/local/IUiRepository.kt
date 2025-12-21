package com.riders.thelab.core.ui.data.local

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.data.local.preferences.IPreferences
import com.riders.thelab.core.ui.data.local.preferences.proto.DarkThemeConfigProto
import com.riders.thelab.core.ui.data.local.preferences.proto.ThemeColorConfigProto
import kotlinx.coroutines.flow.Flow

interface IUiRepository : IPreferences {
    fun getThemeColorSync(): ThemeColorConfigProto
    fun getThemeColorAsAppTheme(): Flow<AppTheme>
    fun getThemeColorAsAppThemeSync(): AppTheme

    fun getThemeDarkConfigSync(): DarkThemeConfigProto


    fun isDarkTheme(): Flow<Boolean?>

    @Composable
    fun isDarkThemeComposable(isSystemInDark: Boolean = isSystemInDarkTheme()): Flow<Boolean>
}