package com.riders.thelab.di

import android.app.Application
import android.content.Context
import com.riders.thelab.TheLabApplication
import com.riders.thelab.core.common.network.LabNetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTheLabApplication(): TheLabApplication = TheLabApplication.getInstance()

    @Provides
    @Singleton
    fun providesLabNetworkManager(@ApplicationContext context: Context) =
        LabNetworkManager.getInstance(context = context)
}