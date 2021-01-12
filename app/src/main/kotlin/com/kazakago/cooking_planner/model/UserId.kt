package com.kazakago.cooking_planner.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = UserId.Serializer::class)
data class UserId(val value: Long) {

    object Serializer : KSerializer<UserId> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("id", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, value: UserId) {
            encoder.encodeLong(value.value)
        }

        override fun deserialize(decoder: Decoder): UserId {
            return UserId(decoder.decodeLong())
        }
    }
}
