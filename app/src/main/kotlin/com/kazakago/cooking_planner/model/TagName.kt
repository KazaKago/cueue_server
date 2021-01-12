package com.kazakago.cooking_planner.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = TagName.Serializer::class)
data class TagName(val value: String) {

    object Serializer : KSerializer<TagName> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("id", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: TagName) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): TagName {
            return TagName(decoder.decodeString())
        }
    }
}
