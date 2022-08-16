package com.kazakago.cueue.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = TagId.Serializer::class)
data class TagId(val value: Long) {

    object Serializer : KSerializer<TagId> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("tag_id", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, value: TagId) {
            encoder.encodeLong(value.value)
        }

        override fun deserialize(decoder: Decoder): TagId {
            return TagId(decoder.decodeLong())
        }
    }
}
