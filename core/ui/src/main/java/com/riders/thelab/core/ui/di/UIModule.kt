package com.riders.thelab.core.ui.di

import android.content.Context
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.core.ui.data.local.UiRepository
import com.riders.thelab.core.ui.data.local.preferences.IPreferences
import com.riders.thelab.core.ui.data.local.preferences.PreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UIModule {

    @Provides
    @Singleton
    fun provideUiPreferences(
        @ApplicationContext context: Context
    ): IPreferences = PreferencesImpl(context = context) as IPreferences

    @Provides
    @Singleton
    fun provideUiRepository(
        uiPreferences: IPreferences
    ): IUiRepository = UiRepository(uiPreferences) as IUiRepository
}