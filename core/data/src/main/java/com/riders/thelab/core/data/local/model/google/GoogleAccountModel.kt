package com.riders.thelab.core.data.local.model.google

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.serialization.Contextual
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import org.kotools.types.EmailAddress
import org.kotools.types.ExperimentalKotoolsTypesApi
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
    idToken = idToken.toNotBlankString().getOrThrow(),
    firstName = givenName?.toNotBlankString()?.getOrThrow(),
    familyName = familyName?.toNotBlankString()?.getOrThrow(),
    displayName = displayName?.toNotBlankString()?.getOrThrow(),
    phoneNumber = phoneNumber?.toNotBlankString()?.getOrThrow(),
    profilePictureUri = profilePictureUri?.toString()?.toNotBlankString()?.getOrThrow()
)

@OptIn(ExperimentalKotoolsTypesApi::class)
fun GoogleSignInAccount.toGoogleAccountModel() = GoogleAccountModel(
    emailAddress = email?.let { EmailAddress.orThrow(it) } ?: EmailAddress.orThrow("na@na.com"),
    idToken = idToken?.toNotBlankString()?.getOrThrow() ?: "N/A".toNotBlankString().getOrThrow(),
    firstName = givenName?.toNotBlankString()?.getOrThrow(),
    familyName = familyName?.toNotBlankString()?.getOrThrow(),
    displayName = displayName?.toNotBlankString()?.getOrThrow(),
    phoneNumber = "N/A".toNotBlankString().getOrThrow(),
    profilePictureUri = photoUrl?.toString()?.toNotBlankString()?.getOrThrow()
)