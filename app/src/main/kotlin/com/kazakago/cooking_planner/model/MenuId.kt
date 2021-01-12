package com.kazakago.cooking_planner.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = MenuId.Serializer::class)
data class MenuId(val value: Long) {

    object Serializer : KSerializer<MenuId> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("menu_id", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, value: MenuId) {
            encoder.encodeLong(value.value)
        }

        override fun deserialize(decoder: Decoder): MenuId {
            return MenuId(decoder.decodeLong())
        }
    }
}
