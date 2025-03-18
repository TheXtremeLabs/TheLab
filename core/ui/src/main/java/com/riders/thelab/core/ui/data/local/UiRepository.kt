package com.riders.thelab.core.ui.data.local

import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.data.local.preferences.PreferencesImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UiRepository @Inject constructor(preferencesImpl: PreferencesImpl) : IUiRepository {

    private val mPreferencesImpl: PreferencesImpl = preferencesImpl

    // Implement methods here for data operations
    override suspend fun updateThemeDarkMode(isDarkMode: Boolean) =
        mPreferencesImpl.updateThemeDarkMode(isDarkMode)

    override fun isThemeDarkMode(): Flow<Boolean> = mPreferencesImpl.isThemeDarkMode()

    override suspend fun updateTheme(appTheme: AppTheme) = mPreferencesImpl.updateTheme(appTheme)

    override fun getTheme(): Flow<AppTheme> = mPreferencesImpl.getTheme()

    override suspend fun deleteAllData() = mPreferencesImpl.deleteAllData()
}