package com.riders.thelab.core.data.serializer

import com.google.firebase.Timestamp
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


/**
 * Custom Kotlin Serializer that serializer Google firebase Timestamp to Long value
 */
object FirebaseTimestampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeLong(value.seconds)
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        return Timestamp(decoder.decodeLong(), decoder.decodeInt())
    }
}