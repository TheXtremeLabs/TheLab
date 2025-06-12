package com.riders.thelab.core.data.remote.dto.acrcloud

import kotlinx.serialization.SerialName
import kotools.types.number.AnyInt
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class ACRCloudStatus(
    @SerialName("msg")
    val message: NotBlankString,
    @SerialName("code")
    val code: AnyInt,
    @SerialName("version")
    val version: NotBlankString
) : Serializable
