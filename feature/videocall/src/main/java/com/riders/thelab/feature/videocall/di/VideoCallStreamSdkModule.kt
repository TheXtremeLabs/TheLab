package com.riders.thelab.feature.videocall.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VideoCallStreamSdkModule {

    @Provides
    @Singleton
    fun provideStreamVideo(
        @ApplicationContext context: Context
    ): StreamVideo = StreamVideoBuilder(
        context = context,
        apiKey = "djmy2f7dpjk8",
        user = User(
            id = "Mike",
            name = "Mike",
            type = UserType.Guest
        )
    )
        .build()
}