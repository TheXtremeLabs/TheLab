package com.riders.thelab.core.speechtotext

import android.content.Context
import com.google.api.gax.core.CredentialsProvider
import com.google.auth.Credentials
import com.google.auth.oauth2.ServiceAccountCredentials

class GoogleSpeechCredentialsProvider(private val context: Context) : CredentialsProvider {

    override fun getCredentials(): Credentials = context
        .resources
        .openRawResource(R.raw.creds_google_cloud_the_lab_service)
        .let {
            ServiceAccountCredentials.fromStream(it)
        }

}