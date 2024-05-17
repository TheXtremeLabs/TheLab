package com.riders.thelab.feature.googledrive.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.riders.thelab.core.data.local.model.google.GoogleAccountModel
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import kotools.types.web.EmailAddress

@OptIn(ExperimentalKotoolsTypesApi::class)
fun GoogleIdTokenCredential.toGoogleAccountModel() = GoogleAccountModel(
    emailAddress = EmailAddress.create(id),
    idToken = NotBlankString.create(idToken),
    firstName = if (null == givenName) null else NotBlankString.create(givenName),
    familyName = if (null == familyName) null else NotBlankString.create(familyName),
    displayName = if (null == displayName) null else NotBlankString.create(displayName),
    phoneNumber = if (null == phoneNumber) null else NotBlankString.create(phoneNumber),
    profilePictureUri = if (null == profilePictureUri) null else NotBlankString.create(profilePictureUri.toString())
)

@OptIn(ExperimentalKotoolsTypesApi::class)
fun GoogleSignInAccount.toGoogleAccountModel() = GoogleAccountModel(
    emailAddress = if (null == email) EmailAddress.create("na@na.com") else EmailAddress.create(email!!),
    idToken = if (null == idToken) NotBlankString.create("N/A") else NotBlankString.create(idToken!!),
    firstName = if (null == givenName) null else NotBlankString.create(givenName),
    familyName = if (null == familyName) null else NotBlankString.create(familyName),
    displayName = if (null == displayName) null else NotBlankString.create(displayName),
    phoneNumber = NotBlankString.create("N/A"),
    profilePictureUri = if (null == photoUrl) null else NotBlankString.create(photoUrl.toString())
)