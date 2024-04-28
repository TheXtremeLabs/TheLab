package com.riders.thelab.core.data.remote.dto.wikimedia

import kotlinx.serialization.SerialName
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Parsed(
    @SerialName("title")
    val title:NotBlankString,
    @SerialName("text")
    val text :NotBlankString
): Serializable
