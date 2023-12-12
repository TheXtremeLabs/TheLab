package com.riders.thelab.core.data.local.model.kat

import com.google.firebase.Timestamp
import com.riders.thelab.core.data.serializer.FirebaseTimestampSerializer
import java.io.Serializable

/**
 * Representing Kat Message item for Firebase Cloud Messaging message
 */
@kotlinx.serialization.Serializable
data class KatModel(
    val message: String,
    val senderId: String,
    @kotlinx.serialization.Serializable(with = FirebaseTimestampSerializer::class)
    val timestamp: Timestamp
) : Serializable
