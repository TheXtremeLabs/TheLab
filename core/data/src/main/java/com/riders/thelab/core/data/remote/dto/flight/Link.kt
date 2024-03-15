package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Link(@SerialName("next") private val next: String) : Serializable {

    fun getNext(): String {
        return this.next.split("cursor=").last()
    }
}
