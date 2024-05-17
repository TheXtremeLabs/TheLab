package com.riders.thelab.core.data.local.model.google

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import kotools.types.web.EmailAddress
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
@OptIn(ExperimentalKotoolsTypesApi::class)
data class GoogleAccountModel(
    val emailAddress: EmailAddress,
    val idToken: NotBlankString,
    val firstName: NotBlankString?,
    val familyName: NotBlankString?,
    val displayName: NotBlankString?,
    val phoneNumber: NotBlankString?,
    val profilePictureUri: NotBlankString?,
) : Serializable