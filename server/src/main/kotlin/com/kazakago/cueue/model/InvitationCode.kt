package com.kazakago.cueue.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = InvitationCode.Serializer::class)
data class InvitationCode(val value: String) {

    companion object {
        fun generate() = InvitationCode((10000000..99999999).random().toString())
    }

    object Serializer : KSerializer<InvitationCode> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("invitation_code", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: InvitationCode) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): InvitationCode {
            return InvitationCode(decoder.decodeString())
        }
    }
}