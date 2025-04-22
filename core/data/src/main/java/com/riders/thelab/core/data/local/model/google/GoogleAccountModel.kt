package com.riders.thelab.core.data.local.model.google

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.serialization.Contextual
import org.kotools.types.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import org.kotools.types.EmailAddress
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
@OptIn(ExperimentalKotoolsTypesApi::class)
data class GoogleAccountModel(
    @Contextual val emailAddress: EmailAddress,
    val idToken: NotBlankString,
    val firstName: NotBlankString?,
    val familyName: NotBlankString?,
    val displayName: NotBlankString?,
    val phoneNumber: NotBlankString?,
    val profilePictureUri: NotBlankString?,
) : Serializable


@OptIn(ExperimentalKotoolsTypesApi::class)
fun GoogleIdTokenCredential.toGoogleAccountModel() = GoogleAccountModel(
    emailAddress = EmailAddress.orThrow(id),
    idToken = NotBlankString.create(idToken),
    firstName = if (null == givenName) null else NotBlankString.create(givenName!!),
    familyName = if (null == familyName) null else NotBlankString.create(familyName!!),
    displayName = if (null == displayName) null else NotBlankString.create(displayName!!),
    phoneNumber = if (null == phoneNumber) null else NotBlankString.create(phoneNumber!!),
    profilePictureUri = if (null == profilePictureUri) null else NotBlankString.create(
        profilePictureUri.toString()
    )
)

@OptIn(ExperimentalKotoolsTypesApi::class)
fun GoogleSignInAccount.toGoogleAccountModel() = GoogleAccountModel(
    emailAddress = if (null == email) EmailAddress.orThrow("na@na.com") else EmailAddress.orThrow(
        email!!
    ),
    idToken = if (null == idToken) NotBlankString.create("N/A") else NotBlankString.create(idToken!!),
    firstName = if (null == givenName) null else NotBlankString.create(givenName!!),
    familyName = if (null == familyName) null else NotBlankString.create(familyName!!),
    displayName = if (null == displayName) null else NotBlankString.create(displayName!!),
    phoneNumber = NotBlankString.create("N/A"),
    profilePictureUri = if (null == photoUrl) null else NotBlankString.create(photoUrl.toString())
)