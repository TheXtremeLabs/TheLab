package com.riders.thelab.di

import android.content.Context
import com.google.auth.Credentials
import com.google.cloud.speech.v1.SpeechClient
import com.google.cloud.speech.v1.stub.GrpcSpeechStub
import com.google.cloud.speech.v1.stub.SpeechStubSettings
import com.riders.thelab.core.speechtotext.Constants
import com.riders.thelab.core.speechtotext.GoogleSpeechCredentialsProvider
import com.riders.thelab.core.speechtotext.ISpeechToTextRepository
import com.riders.thelab.core.speechtotext.SpeechToTextRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VoiceAssistantModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(context = Dispatchers.IO)

    @Provides
    @Singleton
    fun provideGoogleCredentials(@ApplicationContext context: Context): Credentials =
        GoogleSpeechCredentialsProvider(context).credentials

    @Provides
    @Singleton
    fun provideSpeechClient(@ApplicationContext context: Context): SpeechClient {
        var grpcStub: GrpcSpeechStub? = null
        SpeechStubSettings.newBuilder()?.apply {
            credentialsProvider =
                GoogleSpeechCredentialsProvider(context)
            endpoint = "${Constants.HOSTNAME}:${Constants.PORT}"
            grpcStub = GrpcSpeechStub.create(this.build())
        }?.build()
        return SpeechClient.create(grpcStub)
    }

    @Provides
    @Singleton
    fun provideSpeechToTextRepository(
        speechClient: SpeechClient,
        coroutineScope: CoroutineScope
    ): ISpeechToTextRepository =
        SpeechToTextRepository(speechClient, coroutineScope)
}