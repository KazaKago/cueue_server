package com.kazakago.cueue.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = WorkspaceId.Serializer::class)
data class WorkspaceId(val value: Long) {

    object Serializer : KSerializer<WorkspaceId> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("workspace_id", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, value: WorkspaceId) {
            encoder.encodeLong(value.value)
        }

        override fun deserialize(decoder: Decoder): WorkspaceId {
            return WorkspaceId(decoder.decodeLong())
        }
    }
}
