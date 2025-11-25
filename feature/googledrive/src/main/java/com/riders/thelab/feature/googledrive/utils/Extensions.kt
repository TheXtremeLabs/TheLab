package com.riders.thelab.feature.googledrive.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.riders.thelab.core.data.local.model.google.GoogleAccountModel
import kotools.types.text.toNotBlankString
import org.kotools.types.EmailAddress
import org.kotools.types.ExperimentalKotoolsTypesApi

@OptIn(ExperimentalKotoolsTypesApi::class)
fun GoogleIdTokenCredential.toGoogleAccountModel() = GoogleAccountModel(
    emailAddress = requireNotNull(EmailAddress of id),
    idToken = idToken.toNotBlankString().getOrThrow(),
    firstName = givenName?.toNotBlankString()?.getOrThrow(),
    familyName = familyName?.toNotBlankString()?.getOrThrow(),
    displayName = displayName?.toNotBlankString()?.getOrThrow(),
    phoneNumber = phoneNumber?.toNotBlankString()?.getOrThrow(),
    profilePictureUri = profilePictureUri?.toString()?.toNotBlankString()?.getOrThrow()
)

@OptIn(ExperimentalKotoolsTypesApi::class)
fun GoogleSignInAccount.toGoogleAccountModel() = GoogleAccountModel(
    emailAddress = email?.let { requireNotNull(EmailAddress of it) } ?: requireNotNull(EmailAddress of "na@na.com"),
    idToken = idToken?.toNotBlankString()?.getOrThrow() ?: "N/A".toNotBlankString().getOrThrow(),
    firstName = givenName?.toNotBlankString()?.getOrThrow(),
    familyName = familyName?.toNotBlankString()?.getOrThrow(),
    displayName = displayName?.toNotBlankString()?.getOrThrow(),
    phoneNumber = "N/A".toNotBlankString().getOrThrow(),
    profilePictureUri = photoUrl?.toString()?.toNotBlankString()?.getOrThrow()
)