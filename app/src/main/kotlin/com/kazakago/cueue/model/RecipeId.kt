package com.kazakago.cueue.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RecipeId.Serializer::class)
data class RecipeId(val value: Long) {

    object Serializer : KSerializer<RecipeId> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("recipe_id", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, value: RecipeId) {
            encoder.encodeLong(value.value)
        }

        override fun deserialize(decoder: Decoder): RecipeId {
            return RecipeId(decoder.decodeLong())
        }
    }
}
