package com.kazakago.cooking_planner.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Email.Serializer::class)
data class Email(val value: String) {

    object Serializer : KSerializer<Email> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("email", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Email) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): Email {
            return Email(decoder.decodeString())
        }
    }
}
