package com.kazakago.cueue.model

import io.ktor.auth.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = UID.Serializer::class)
data class UID(val value: String) : Principal {

    object Serializer : KSerializer<UID> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("uid", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: UID) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): UID {
            return UID(decoder.decodeString())
        }
    }
}
