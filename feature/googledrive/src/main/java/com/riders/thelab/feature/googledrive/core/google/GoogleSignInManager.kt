package com.riders.thelab.feature.googledrive.core.google

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.credentials.GetCredentialException
import android.os.Build
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import kotlinx.coroutines.launch
import timber.log.Timber
import java.security.MessageDigest
import java.util.UUID


class GoogleSignInManager(private val context: Context) : GoogleKeyValidation() {

    // Use your app or activity context to instantiate a client instance of CredentialManager.
    private val credentialManager = CredentialManager.create(context)


    suspend fun signIn() {
        Timber.d("signIn()")

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(com.riders.thelab.core.ui.R.string.server_client_id))
            .setAutoSelectEnabled(true)
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        runCatching {

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

            Timber.d("signIn() | coroutineScope | google ID token: $googleIdToken")
        }
            .onFailure { exception ->
                Timber.e("signIn() | onFailure | error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")

                if (LabCompatibilityManager.isTiramisu()) {
                    @SuppressLint("NewApi")
                    when (exception) {
                        is GetCredentialException -> exception.printStackTrace()
                    }
                }
                
                when (exception) {
                    is GoogleIdTokenParsingException -> exception.printStackTrace()
                    else -> {}
                }
                // Timber.e("signIn() | onFailure | error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.d("signIn() | onSuccess | is success: $it")
            }
    }


    fun handleGoogleSignInResult(requestCode: Int, intent: Intent) {
        Timber.d("handleGoogleSignInResult()")

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

            if (task.isSuccessful) {
                // Sign in succeeded, proceed with account
                val account: GoogleSignInAccount = task.result
                Timber.d("handleGoogleSignInResult() | Sign in succeeded, proceed with accountSign in succeeded, proceed with account")
            } else {
                // Sign in failed, handle failure and update UI
                Timber.e("handleGoogleSignInResult() | Sign in failed, handle failure and update UI")

                // ...
            }
        }
    }


    companion object {
        private const val RC_SIGN_IN: Int = 9001
    }

}