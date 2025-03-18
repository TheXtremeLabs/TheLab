package com.riders.thelab.core.ui.di

import android.content.Context
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.core.ui.data.local.UiRepository
import com.riders.thelab.core.ui.data.local.preferences.IPreferences
import com.riders.thelab.core.ui.data.local.preferences.PreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object UIModule {

    @Provides
    @Singleton
    fun provideUiPreferencesImpl(@ApplicationContext context: Context): IPreferences =
        PreferencesImpl(context = context)

    @Provides
    @Singleton
    fun provideUiRepository(uiPreferences: PreferencesImpl): IUiRepository =
        UiRepository(preferencesImpl = uiPreferences)
}