package com.riders.thelab.feature.videocall.di

import android.content.Context
import com.riders.thelab.feature.videocall.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.log.Priority
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.logging.LoggingLevel
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserToken
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
        apiKey = Constants.STREAM_SDK_API_KEY,
        user = User(
            id = "Mike",
            name = "Mike",
            image = "https://bit.ly/2TIt8NR",
            type = UserType.Guest
        ),
        geo = GEO.GlobalEdgeNetwork, // Choose appropriate geo region
        token = Constants.STREAM_SDK_TOKEN_KEY,
        // set the logging level
        loggingLevel = LoggingLevel(priority = Priority.DEBUG),
        )
        .build()
}