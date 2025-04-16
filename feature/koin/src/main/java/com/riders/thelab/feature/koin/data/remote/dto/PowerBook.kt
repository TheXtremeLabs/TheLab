package com.riders.thelab.feature.koin.data.remote.dto

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class PowerBook(val title: String) : java.io.Serializable {

    @OptIn(ExperimentalUuidApi::class)
    val uuid: String get() = Uuid.Companion.random().toString()
}