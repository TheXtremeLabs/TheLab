package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class OperatorResponse(
    @SerialName("num_pages")
    val maxPages: Int,
    @SerialName("operators")
    val operators: List<Operator>,
    @SerialName("links")
    val links: Link
) : Serializable
